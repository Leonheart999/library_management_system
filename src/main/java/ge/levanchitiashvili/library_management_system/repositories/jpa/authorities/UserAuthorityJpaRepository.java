package ge.levanchitiashvili.library_management_system.repositories.jpa.authorities;


import ge.levanchitiashvili.library_management_system.config.BaseRepository;
import ge.levanchitiashvili.library_management_system.models.security.UserAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthorityJpaRepository extends BaseRepository<UserAuthority,Long> {

    UserAuthority findFirstByUserIdAndAuthorityId(long userId, long authorityId);

    List<UserAuthority> findByUserIdAndActiveTrue(long id);
}
