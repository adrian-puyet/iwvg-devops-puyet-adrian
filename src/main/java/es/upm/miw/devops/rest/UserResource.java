package es.upm.miw.devops.rest;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.rest.dto.ActiveStatusRequest;
import es.upm.miw.devops.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}/active")
    public ResponseEntity<User> updateActive(@PathVariable String id, @RequestBody ActiveStatusRequest request) {
        return ResponseEntity.ok(userService.updateActive(id, request.isActive()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}