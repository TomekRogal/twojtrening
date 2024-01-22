package pl.coderslab.twojtrening.trainingexercise;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.coderslab.twojtrening.exercise.Exercise;
import pl.coderslab.twojtrening.exercise.ExerciseRepository;
import pl.coderslab.twojtrening.training.Training;
import pl.coderslab.twojtrening.training.TrainingRepository;
import pl.coderslab.twojtrening.user.User;
import pl.coderslab.twojtrening.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TrainingExerciseRepositoryTest {
    @Autowired
    private TrainingExerciseRepository underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }
    @Test
    void shouldFindAllExercisesFromTraining() {
        //given
        User user = User.builder()
                .username("test").password("test").enabled(1)
                .build();
        Training training = Training.builder()
                .user(user).name("test")
                .build();
        Exercise exercise = Exercise.builder()
                .name("test")
                .build();
        TrainingExercise trainingExercise1 = TrainingExercise.builder()
                .training(training).exercise(exercise).sets(1).weight(1.0).reps(1)
                .build();
        TrainingExercise trainingExercise2 = TrainingExercise.builder()
                .training(training).exercise(exercise).sets(1).weight(1.0).reps(1)
                .build();
        userRepository.save(user);
        trainingRepository.save(training);
        exerciseRepository.save(exercise);
        underTest.save(trainingExercise1);
        underTest.save(trainingExercise2);
        //when
        List<TrainingExercise> allExercisesFromTraining = underTest.findAllExercisesFromTraining(training);
        //then
        assertThat(allExercisesFromTraining.size()).isEqualTo(2);
    }

    @Test
    void shouldDeleteAllFromTraining() {
        User user = User.builder()
                .username("test").password("test").enabled(1)
                .build();
        Training training = Training.builder()
                .user(user).name("test")
                .build();
        Exercise exercise = Exercise.builder()
                .name("test")
                .build();
        TrainingExercise trainingExercise1 = TrainingExercise.builder()
                .training(training).exercise(exercise).sets(1).weight(1.0).reps(1)
                .build();
        TrainingExercise trainingExercise2 = TrainingExercise.builder()
                .training(training).exercise(exercise).sets(1).weight(1.0).reps(1)
                .build();
        userRepository.save(user);
        trainingRepository.save(training);
        exerciseRepository.save(exercise);
        underTest.save(trainingExercise1);
        underTest.save(trainingExercise2);
        //when
        underTest.deleteAllFromTraining(training);
        List<TrainingExercise> allExercisesFromTraining = underTest.findAllExercisesFromTraining(training);
        //then
        assertThat(allExercisesFromTraining.size()).isEqualTo(0);
    }
}