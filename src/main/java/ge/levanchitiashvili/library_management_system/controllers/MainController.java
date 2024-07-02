package ge.levanchitiashvili.library_management_system.controllers;

import ge.levanchitiashvili.library_management_system.config.util.JwtUtils;
import ge.levanchitiashvili.library_management_system.requests.security.AuthRequest;
import ge.levanchitiashvili.library_management_system.requests.security.RegisterRequest;
import ge.levanchitiashvili.library_management_system.services.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MainController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/register")
    public void welcome(@Valid @RequestBody RegisterRequest registerRequest){
        userService.registerUser(registerRequest);
    }


    @PostMapping("/logIn")
    public String logIn(@Valid @RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtils.generateToken(authRequest.getUserName());
    }
}
