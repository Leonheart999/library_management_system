package ge.levanchitiashvili.library_management_system.services.books;

import ge.levanchitiashvili.library_management_system.dtos.books.BookDTO;
import ge.levanchitiashvili.library_management_system.models.books.Book;
import ge.levanchitiashvili.library_management_system.requests.books.AddEditBookRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {

    List<Book> search(String title, String author, String isbn, Book.Status status, Boolean onlyActive);

    void delete(long id);

    Book get(long id);

    Book edit(long id, AddEditBookRequest addEditBookRequest);

    Book addNew(AddEditBookRequest addEditBookRequest);

    Book borrow(long id);

    Book returnBook(long id);


    List<BookDTO> ENTITY_DTO_List(List<Book> entities);

    Page<BookDTO> ENTITY_DTO_PAGE(Page<Book> entitiesPage) ;

    BookDTO ENTITY_DTO(Book entity);

}
