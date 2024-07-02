package ge.levanchitiashvili.library_management_system.services.books;

import ge.levanchitiashvili.library_management_system.config.EntityToDtoConverter;
import ge.levanchitiashvili.library_management_system.dtos.books.BookDTO;
import ge.levanchitiashvili.library_management_system.models.books.Book;
import ge.levanchitiashvili.library_management_system.repositories.jpa.books.BookJpaRepository;
import ge.levanchitiashvili.library_management_system.requests.books.AddEditBookRequest;
import ge.levanchitiashvili.library_management_system.services.security.SecurityService;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl extends EntityToDtoConverter<Book, BookDTO> implements BookService {
    private final BookJpaRepository bookJpaRepository;
    private final JmsTemplate jmsTemplate;

    @Override
    public List<Book> search(String title, String author, String isbn, Book.Status status, Boolean onlyActive) {
        return bookJpaRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (BooleanUtils.isTrue(onlyActive)) {
                predicate = cb.and(predicate, cb.equal(root.get(Book.Fields.active), true));
            }
            if (!StringUtils.isEmpty(title)) {
                predicate = cb.and(predicate, cb.like(root.get(Book.Fields.title), "%" + title + "%"));
            }

            if (!StringUtils.isEmpty(author)) {
                predicate = cb.and(predicate, cb.like(root.get(Book.Fields.author), "%" + author + "%"));
            }

            if (!StringUtils.isEmpty(isbn)) {
                predicate = cb.and(predicate, cb.like(root.get(Book.Fields.isbn), "%" + isbn + "%"));
            }

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Book.Fields.status), status));
            }

            return predicate;
        });
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(long id) {
        Book book = get(id);
        book.setUserId(null);
        book.setStatus(Book.Status.AVAILABLE);
        book.setActive(false);
        save(book);
    }

    @Override
    public Book get(long id) {
        Book book = bookJpaRepository.findById(id).orElse(null);
        if (book == null) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }
        return book;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Book edit(long id, AddEditBookRequest addEditBookRequest) {
        Book book = get(id);
        book.setAuthor(addEditBookRequest.getAuthor());
        book.setTitle(addEditBookRequest.getTitle());
        book.setIsbn(addEditBookRequest.getIsbn());
        validateBook(book);
        return save(book);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Book addNew(AddEditBookRequest addEditBookRequest) {
        Book book = new Book();
        book.setIsbn(addEditBookRequest.getIsbn());
        book.setTitle(addEditBookRequest.getTitle());
        book.setAuthor(addEditBookRequest.getAuthor());
        book.setStatus(Book.Status.AVAILABLE);
        book.setActive(true);
        validateBook(book);
        return save(book);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Book borrow(long id) {
        long userId = SecurityService.getCurrentUserId();
        Book book = get(id);
        if (book.getStatus().equals( Book.Status.BORROWED)) {
            if (userId == book.getUserId()) {
                throw new RuntimeException("Book with id " + id + " is already borrowed by current user");
            }
            throw new RuntimeException("Book with id " + id + " is already borrowed");
        }
        book.setStatus(Book.Status.BORROWED);
        book.setUserId(userId);
        book = save(book);

        jmsTemplate.convertAndSend("bookQueue", "Book with id " + book.getId() + " borrowed by user " + userId);

        return book;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Book returnBook(long id) {
        long userId = SecurityService.getCurrentUserId();
        Book book = get(id);
        if (book.getStatus().equals(Book.Status.AVAILABLE)) {
            throw new RuntimeException("Book with id " + id + " is not borrowed");
        }
        if (userId != book.getUserId()) {
            throw new RuntimeException("Book with id " + id + " is not borrowed by current user");
        }
        book.setStatus(Book.Status.AVAILABLE);
        book.setUserId(null);
        book = save(book);
        jmsTemplate.convertAndSend("bookQueue", "Book with id " + book.getId() + " returned by user " + userId);
        return book;
    }

    private void validateBook(Book book){
        Book checker=bookJpaRepository.findBookByTitleAndActiveTrue(book.getTitle());
        if(book.getId()==null || !checker.getId().equals(book.getId())){
            throw new RuntimeException("Book with title " + book.getTitle() + " already exists");
        }

        checker=bookJpaRepository.findBookByIsbnAndActiveTrue(book.getIsbn());
        if(book.getId()==null || !checker.getId().equals(book.getId())){
            throw new RuntimeException("Book with ISBN " + book.getIsbn() + " already exists");
        }
    }
    private Book save(Book book) {
        return bookJpaRepository.save(book);
    }
}
