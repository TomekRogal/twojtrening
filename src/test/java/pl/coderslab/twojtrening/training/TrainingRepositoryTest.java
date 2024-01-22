package pl.coderslab.twojtrening.training;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.coderslab.twojtrening.user.User;
import pl.coderslab.twojtrening.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TrainingRepositoryTest {
    @Autowired
    private TrainingRepository underTest;
    @Autowired
    private UserRepository userRepository;
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindAllTrainingByUser() {
        //given
        User user = User.builder()
                .username("test").password("test").enabled(1)
                .build();
        Training training1 = Training.builder()
                .user(user).name("test")
                .build();
        Training training2 = Training.builder()
                .user(user).name("test2")
                .build();
        userRepository.save(user);
        underTest.save(training1);
        underTest.save(training2);
        //when
        List<Training> trainingsByUser = underTest.findByUser(user);
        //then
        assertThat(trainingsByUser.size()).isEqualTo(2);
    }

    @Test
    void shouldDeleteAllTrainingsFromUser() {
        //given
        User user = User.builder()
                .username("test").password("test").enabled(1)
                .build();
        Training training1 = Training.builder()
                .user(user).name("test")
                .build();
        Training training2 = Training.builder()
                .user(user).name("test2")
                .build();
        userRepository.save(user);
        underTest.save(training1);
        underTest.save(training2);
        //when
        underTest.deleteAllTrainingsFromUser(user);
        List<Training> trainingsByUser = underTest.findByUser(user);
        //then
        assertThat(trainingsByUser.size()).isEqualTo(0);
    }
}