package es.upm.miw.devops.rest;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.rest.dto.ActiveStatusRequest;
import es.upm.miw.devops.rest.dto.UserActiveStatusItem;
import es.upm.miw.devops.rest.dto.UserUpdateRequest;
import es.upm.miw.devops.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) Boolean billable) {
        return ResponseEntity.ok(userService.getUsers(billable));
    }


    @PutMapping("/{id}/active")
    public ResponseEntity<User> updateActive(@PathVariable Long id, @RequestBody ActiveStatusRequest request) {
        return ResponseEntity.ok(userService.updateActive(id, request.isActive()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PatchMapping
    public ResponseEntity<List<User>> batchUpdateActive(@RequestBody @Valid List<@Valid UserActiveStatusItem> items) {
        return ResponseEntity.ok(userService.batchUpdateActive(items));
    }
}