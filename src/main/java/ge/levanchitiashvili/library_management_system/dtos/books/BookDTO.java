package ge.levanchitiashvili.library_management_system.dtos.books;

import ge.levanchitiashvili.library_management_system.models.books.Book;
import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Book.Status status;
    private Long userId;
    private Boolean active;
}
