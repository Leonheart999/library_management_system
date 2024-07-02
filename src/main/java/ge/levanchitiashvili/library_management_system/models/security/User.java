package ge.levanchitiashvili.library_management_system.models.security;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ge.levanchitiashvili.library_management_system.models.books.Book;
import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Data
@Entity
@Table(schema = "library",name = "users")
@SequenceGenerator(name = "usersIdSeq", sequenceName = "library.users_id_seq", allocationSize = 1)
public class User {
    public  enum Authority{
        USER,ADMIN
    }
    @Id
    @GeneratedValue(generator = "userIdSeq",strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "user_name", nullable = false,unique = true)
    private String username;
    @JsonBackReference
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "active")
    private Boolean active;
    @JsonManagedReference("Book")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,mappedBy = "user")
    private List<Book> borrowedBooks;
}
