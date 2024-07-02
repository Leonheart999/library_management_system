package ge.levanchitiashvili.library_management_system.services.users;



import ge.levanchitiashvili.library_management_system.config.EntityToDtoConverter;
import ge.levanchitiashvili.library_management_system.dtos.security.UserDTO;
import ge.levanchitiashvili.library_management_system.models.security.User;
import ge.levanchitiashvili.library_management_system.models.security.UserAuthority;
import ge.levanchitiashvili.library_management_system.repositories.jpa.authorities.UserAuthorityJpaRepository;
import ge.levanchitiashvili.library_management_system.repositories.jpa.users.UserJPARepository;
import ge.levanchitiashvili.library_management_system.requests.security.RegisterRequest;
import ge.levanchitiashvili.library_management_system.requests.users.UserEditRequest;
import ge.levanchitiashvili.library_management_system.services.authorities.AuthorityService;
import ge.levanchitiashvili.library_management_system.services.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends EntityToDtoConverter<User, UserDTO> implements UserService {
    private final UserJPARepository userRepository;
    @Lazy
    private final SecurityService securityService;
    private final AuthorityService authorityService;
    private final UserAuthorityJpaRepository userAuthorityRepository;

    @Override
    public User get(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException(String.format("User with with id %s", id));
        }
        return user.get();
    }

    @Override
    public User getAuthorisedUser() {
        long userId= SecurityService.getCurrentUserId();
        return get(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNew(User user) {
        if (userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException(String.format("User with username %s already exists", user.getUsername()));
        }
        user.setActive(true);
        user.setPassword(securityService.getEncodedPassword(user.getPassword()));
        user=save(user);
        addUserAuthority(user.getId(),User.Authority.USER.ordinal());
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User edit(long id, UserEditRequest userEditRequest) {
        User oldUser = get(id);
        oldUser.setUsername(userEditRequest.getUsername());
        return save(oldUser);
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameAndActiveTrue(username);
    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        securityService.changePassword(oldPassword, newPassword);
    }

    @Override
    public List<String> getUserAuthorityNames(long userId) {
        return userRepository.findUserAuthorities(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserAuthority(long id, long authorityId) {
        get(id);
        authorityService.get(authorityId);
        UserAuthority userAuthority = userAuthorityRepository.findFirstByUserIdAndAuthorityId(id, authorityId);
        if (userAuthority != null) {
            userAuthority.setActive(true);
        } else {
            userAuthority = new UserAuthority();
            userAuthority.setAuthorityId(authorityId);
            userAuthority.setUserId(id);
            userAuthority.setActive(true);
        }
        userAuthorityRepository.save(userAuthority);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerUser(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getRepeatPassword()))
            throw new RuntimeException("repeat password doesn't match");
        User user = new User();
        user.setPassword(registerRequest.getPassword());
        user.setUsername(registerRequest.getUserName());
        addNew(user);
    }

    private void validateUser(User user){
        Optional<User> checker=userRepository.findByUsernameAndActiveTrue(user.getUsername());
        if(checker.isPresent() && (user.getId()==null || !checker.get().getId().equals(user.getId()))){
            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
        }
    }

}
