package es.upm.miw.devops.service;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import es.upm.miw.devops.rest.dto.UserUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User updateUser(String id, UserUpdateRequest request) {
        User user = getUserById(id);

        user.setFirstName(request.getFirstName());
        user.setFamilyName(request.getFamilyName());
        user.setEmail(request.getEmail());
        user.setIdentity(request.getIdentity());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setProvince(request.getProvince());
        user.setPostalCode(request.getPostalCode());

        return userRepository.save(user);
    }

    public User updateActive(String id, boolean active) {
        User user = getUserById(id);
        user.setActive(active);
        return userRepository.save(user);
    }

    public void deleteUserById(String id){
        userRepository.deleteById(id);
    }

    public List<User> getUsers(Boolean billable) {
        List<User> users = userRepository.findAll();

        if (billable == null) {
            return users;
        }

        return users.stream()
                .filter(user -> user.isBillable() == billable)
                .toList();
    }
}