package ge.levanchitiashvili.library_management_system.models.books;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ge.levanchitiashvili.library_management_system.models.security.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@Entity
@Table(schema = "library",name = "books")
@SequenceGenerator(name = "bookIdSeq", sequenceName = "library.books_id_seq", allocationSize = 1)
@FieldNameConstants
public class Book {
    public enum Status{
        BORROWED,AVAILABLE
    }
    @Id
    @GeneratedValue(generator = "bookIdSeq",strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(name = "isbn", nullable = false)
    private String isbn;
    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "active")
    private Boolean active;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
