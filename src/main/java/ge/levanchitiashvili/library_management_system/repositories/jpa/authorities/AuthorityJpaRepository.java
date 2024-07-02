package ge.levanchitiashvili.library_management_system.repositories.jpa.authorities;



import ge.levanchitiashvili.library_management_system.config.BaseRepository;
import ge.levanchitiashvili.library_management_system.models.security.Authority;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityJpaRepository extends BaseRepository<Authority,Long> {
}
