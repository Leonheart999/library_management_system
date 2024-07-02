package ge.levanchitiashvili.library_management_system.dtos.security;

import lombok.Data;

import java.util.List;

@Data
public class AuthorityDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Boolean active;
}
