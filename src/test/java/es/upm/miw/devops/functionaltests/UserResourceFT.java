package es.upm.miw.devops.functionaltests;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import es.upm.miw.devops.rest.dto.ActiveStatusRequest;
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

    @Test
    void testPutUserActive() {
        savedUser = userRepository.save(
                new User("John", "Doe", "john.doe@example.com"));
        webTestClient.put()
                .uri("/user/{id}/active", savedUser.getId())
                .bodyValue(new ActiveStatusRequest(true))
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> assertThat(user)
                        .isNotNull()
                        .extracting(User::getId, User::isActive)
                        .containsExactly(savedUser.getId(), true));
    }
    @Test
    void testPutUserNotActive() {
        savedUser = userRepository.save(
                new User("John", "Doe", "john.doe@example.com"));
        webTestClient.put()
                .uri("/user/{id}/active", savedUser.getId())
                .bodyValue(new ActiveStatusRequest(false))
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> assertThat(user)
                        .isNotNull()
                        .extracting(User::getId, User::isActive)
                        .containsExactly(savedUser.getId(), false));
    }

    @Test
    void testDeleteUser(){
        savedUser = userRepository.save(
                new User("John", "Doe", "john.doe@example.com"));
        webTestClient.delete()
                .uri("/user/{id}", savedUser.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/user/{id}", savedUser.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testReadBillableUsers() {
        User billableUser = userRepository.save(
                new User(
                        "John",
                        "Doe",
                        "john.doe@example.com",
                        "12345678A",
                        "Calle Mayor 1",
                        "Madrid",
                        "Madrid",
                        "28001"
                )
        );
        User nonBillableUser = userRepository.save(
                new User(
                        "John",
                        "Doe",
                        "john.doe@example.com"
                )
        );

        savedUser = billableUser;

        webTestClient.get()
                .uri("/user?billable=true")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> {
                    assertThat(users)
                            .isNotEmpty()
                            .allMatch(User::isBillable);

                    assertThat(users)
                            .extracting(User::getId)
                            .contains(billableUser.getId());
                });
    }
    @Test
    void testReadNonBillableUsers() {
        User billableUser = userRepository.save(
                new User(
                        "John",
                        "Doe",
                        "john.doe@example.com",
                        "12345678A",
                        "Calle Mayor 1",
                        "Madrid",
                        "Madrid",
                        "28001"
                )
        );
        User nonBillableUser = userRepository.save(
                new User(
                        "John",
                        "Doe",
                        "john.doe@example.com"
                )
        );

        savedUser = nonBillableUser;

        webTestClient.get()
                .uri("/user?billable=false")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> {
                    assertThat(users)
                            .isNotEmpty()
                            .noneMatch(User::isBillable);

                    assertThat(users)
                            .extracting(User::getId)
                            .contains(nonBillableUser.getId());
                });
    }
    @Test
    void testReadUsersWithoutBillableFilter() {
        User billableUser = userRepository.save(
                new User(
                        "John",
                        "Doe",
                        "john.doe@example.com",
                        "12345678A",
                        "Calle Mayor 1",
                        "Madrid",
                        "Madrid",
                        "28001"
                )
        );

        User nonBillableUser = userRepository.save(
                new User(
                        "Jane",
                        "Doe",
                        "jane.doe@example.com"
                )
        );

        savedUser = billableUser;

        webTestClient.get()
                .uri("/user")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> assertThat(users)
                        .extracting(User::getId)
                        .contains(billableUser.getId(), nonBillableUser.getId()));
    }


    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }
}