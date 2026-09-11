package es.upm.miw.devops.functionaltests;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class UserResourceFT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @Test
    void testReadUserById() {
        savedUser = userRepository.save(
                new User("John", "Doe", "john.doe@example.com"));

        webTestClient.get()
                .uri("/user/{id}", savedUser.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> assertThat(user)
                        .isNotNull()
                        .extracting(User::getId, User::getFirstName, User::getFamilyName, User::getEmail)
                        .containsExactly(savedUser.getId(), "John", "Doe", "john.doe@example.com"));
    }

    @Test
    void testReadUserByIdNotFound() {
        webTestClient.get()
                .uri("/user/{id}", "000000000000000000000000")
                .exchange()
                .expectStatus().isNotFound();
    }

    @AfterEach
    void cleanUp() {
        if (savedUser != null) {
            userRepository.deleteById(savedUser.getId());
            savedUser = null;
        }
    }
}