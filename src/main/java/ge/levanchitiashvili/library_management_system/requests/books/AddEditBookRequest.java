package ge.levanchitiashvili.library_management_system.requests.books;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddEditBookRequest {
    @NotBlank(message = "title can not be blank")
    private String title;
    @NotBlank(message = "author can not be blank")
    private String author;
    @NotBlank(message = "isbn can not be blank")
    private String isbn;
}
