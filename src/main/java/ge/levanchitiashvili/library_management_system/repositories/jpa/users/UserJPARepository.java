package ge.levanchitiashvili.library_management_system.repositories.jpa.users;



import ge.levanchitiashvili.library_management_system.config.BaseRepository;
import ge.levanchitiashvili.library_management_system.models.security.User;
import ge.levanchitiashvili.library_management_system.models.security.UserAuthority;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJPARepository extends BaseRepository<User, Long> {
    Optional<User> findByUsernameAndActiveTrue(String username);

//    Boolean existsByEmail(String email);

//    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("select ua.authority.name from UserAuthority ua where ua.active=true and ua.userId=:userId")
    List<String> findUserAuthorities(long userId);

//    @Query("select ua from UserAuthority ua where ua.userId=:userId and ua.authorityId=:authorityId")
//    UserAuthority findUserAuthority(long userId, long authorityId);
}
