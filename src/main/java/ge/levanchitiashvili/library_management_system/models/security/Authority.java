package ge.levanchitiashvili.library_management_system.models.security;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Data
@Table(schema = "library",name = "authorities")
@FieldNameConstants
public class Authority {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "active")
    private Boolean active;
}
