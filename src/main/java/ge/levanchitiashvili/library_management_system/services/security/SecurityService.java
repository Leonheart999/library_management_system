package ge.levanchitiashvili.library_management_system.services.security;



import ge.levanchitiashvili.library_management_system.models.security.SecUser;
import ge.levanchitiashvili.library_management_system.models.security.User;
import ge.levanchitiashvili.library_management_system.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username"));
//
//        Set<GrantedAuthority> authorities = user.getRoles().stream()
//                .map((role) -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toSet());

        return new SecUser(user ,convertToGrantedAuthorities(userService.getUserAuthorityNames(user.getId())));
//        new org.springframework.security.core.userdetails.User(
//                usernameOrEmail,
//                user.getPassword(),
//                authorities
//        );
    }


    public static List<GrantedAuthority> convertToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }



    @NotNull
    public static SecUser getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecUser)
            return (SecUser) auth.getPrincipal();
        else
            return new SecUser(new User());
    }

    public static boolean hasAuthority(String authority) {
        return hasAnyAuthority(authority);
    }

    public static boolean hasAnyAuthority(String... authorities) {
        Set<String> authoritiesSet = Stream.of(authorities)
                .collect(Collectors.toSet());

        return getUser().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authoritiesSet::contains);
    }



    public static @NotNull SecUser getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("Not authorised");
        }
        return (SecUser) auth.getPrincipal();
    }

    public static long getCurrentUserId() {
        var user = getCurrentUser();
        return user.getId();
    }

    public String getEncodedPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void changePassword(String oldPassword, String newPassword) {
        long userId=getCurrentUserId();
        User user = userService.get(userId);
        if(!passwordEncoder.matches(oldPassword,user.getPassword())){
            throw new RuntimeException("invalid old password");
        }
        user.setPassword(getEncodedPassword(newPassword));
        userService.save(user);
    }

}
