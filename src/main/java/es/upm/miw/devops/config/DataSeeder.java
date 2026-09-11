package es.upm.miw.devops.config;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test") // skip seeding during tests
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.saveAll(List.of(
                    new User("John", "Doe", "john.doe@example.com"),
                    new User("Jane", "Smith", "jane.smith@example.com",
                            "12345678A", "Calle Mayor 1", "Madrid", "Madrid", "28001"),
                    new User("Carlos", "Garcia", "carlos.garcia@example.com",
                            "87654321B", "Gran Via 20", "Barcelona", "Barcelona", "08001")
            ));
        }
    }
}