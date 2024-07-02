package ge.levanchitiashvili.library_management_system.services.authorities;



import ge.levanchitiashvili.library_management_system.dtos.security.AuthorityDTO;
import ge.levanchitiashvili.library_management_system.models.security.Authority;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorityService {
    List<Authority> search(String name);

    Authority get(long id);

    public List<AuthorityDTO> ENTITY_DTO_List(List<Authority> entities);

    public Page<AuthorityDTO> ENTITY_DTO_PAGE(Page<Authority> entitiesPage);

    public AuthorityDTO ENTITY_DTO(Authority entity);

     AuthorityDTO AUTHORITY_DTO(Authority authority);
}
