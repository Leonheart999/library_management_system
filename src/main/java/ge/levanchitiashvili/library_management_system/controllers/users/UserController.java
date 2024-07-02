package ge.levanchitiashvili.library_management_system.controllers.users;



import ge.levanchitiashvili.library_management_system.dtos.security.UserDTO;
import ge.levanchitiashvili.library_management_system.requests.users.UserEditRequest;
import ge.levanchitiashvili.library_management_system.services.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
public class UserController {
    private final UserService userService;

    @GetMapping("current")
    public UserDTO getCurrentUser() {
        return userService.ENTITY_DTO(userService.getAuthorisedUser());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserDTO get(@PathVariable long id) {
         return userService.ENTITY_DTO(userService.get(id));
    }


    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserDTO edit(@PathVariable long id, @Valid @RequestBody UserEditRequest userEditRequest) {
        return userService.ENTITY_DTO(userService.edit(id, userEditRequest));
    }

    @PostMapping("{id}/authority")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void addAuthorityToUser(@PathVariable long id,@RequestParam long authorityId){
            userService.addUserAuthority(id,authorityId);
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestParam String oldPassword,@RequestParam String newPassword){
        userService.changePassword(oldPassword,newPassword);
    }


}
