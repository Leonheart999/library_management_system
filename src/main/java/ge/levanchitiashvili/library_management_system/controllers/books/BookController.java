package ge.levanchitiashvili.library_management_system.controllers.books;

import ge.levanchitiashvili.library_management_system.dtos.books.BookDTO;
import ge.levanchitiashvili.library_management_system.models.books.Book;
import ge.levanchitiashvili.library_management_system.services.books.BookService;
import ge.levanchitiashvili.library_management_system.requests.books.AddEditBookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDTO> search(@RequestParam(required = false) String title,
                                @RequestParam(required = false) String author,
                                @RequestParam(required = false) String isbn,
                                @RequestParam(required = false) Book.Status status,
                                @RequestParam(required = false) Boolean onlyActive
    ) {
        return bookService.ENTITY_DTO_List(bookService.search(title, author, isbn, status, onlyActive));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void delete(@PathVariable long id) {
        bookService.delete(id);
    }

    @GetMapping("{id}")
    public BookDTO get(@PathVariable long id) {

        return bookService.ENTITY_DTO(bookService.get(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public BookDTO edit(@PathVariable long id,@Valid @RequestBody AddEditBookRequest addEditBookRequest) {
        return bookService.ENTITY_DTO(bookService.edit(id,addEditBookRequest ));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public BookDTO save(@Valid @RequestBody AddEditBookRequest addEditBookRequest) {
        return bookService.ENTITY_DTO(bookService.addNew(addEditBookRequest));
    }

    @PostMapping("{id}/borrow")
    public BookDTO borrow( @PathVariable long id){
        return  bookService.ENTITY_DTO(bookService.borrow(id));
    }

    @PostMapping("{id}/return")
    public BookDTO returnBook( @PathVariable long id){
        return bookService.ENTITY_DTO(bookService.returnBook(id));
    }
}
