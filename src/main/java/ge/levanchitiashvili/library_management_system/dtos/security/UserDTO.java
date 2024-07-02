package ge.levanchitiashvili.library_management_system.dtos.security;


import ge.levanchitiashvili.library_management_system.dtos.books.BookDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String newPassword;
    private Boolean active;
    private List<BookDTO> borrowedBooks;
}
