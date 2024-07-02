package ge.levanchitiashvili.library_management_system.services.authorities;


import ge.levanchitiashvili.library_management_system.config.EntityToDtoConverter;
import ge.levanchitiashvili.library_management_system.dtos.security.AuthorityDTO;
import ge.levanchitiashvili.library_management_system.models.security.Authority;
import ge.levanchitiashvili.library_management_system.repositories.jpa.authorities.AuthorityJpaRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl  extends EntityToDtoConverter<Authority,AuthorityDTO> implements AuthorityService{
    private final AuthorityJpaRepository authorityRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Authority> search(String name) {
        return authorityRepository.findAll((root, query, cb)->{
            Predicate predicate = cb.conjunction();
            if(!StringUtils.isEmpty(name)){
                predicate=cb.and(predicate, cb.like(root.get(Authority.Fields.name),"%"+name+"%"));
            }
            return predicate;
        });
    }

    @Override
    public Authority get(long id){
        Optional<Authority> authority =authorityRepository.findById(id);
        if(authority.isEmpty()){
            throw new RuntimeException(String.format("Authority with with id %s", id));
        }
        return authority.get();
    }

    @Override
    public AuthorityDTO AUTHORITY_DTO(Authority authority) {
        return modelMapper.map(authority, AuthorityDTO.class);
    }
}
