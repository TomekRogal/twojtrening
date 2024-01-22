package pl.coderslab.twojtrening.plantraining;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.coderslab.twojtrening.dayname.DayName;
import pl.coderslab.twojtrening.dayname.DayNameRepository;
import pl.coderslab.twojtrening.plan.Plan;
import pl.coderslab.twojtrening.plan.PlanRepository;
import pl.coderslab.twojtrening.training.Training;
import pl.coderslab.twojtrening.training.TrainingRepository;
import pl.coderslab.twojtrening.user.User;
import pl.coderslab.twojtrening.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PlanTrainingRepositoryTest {
    @Autowired
    private PlanTrainingRepository underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private DayNameRepository dayNameRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindAllTrainingsFromPlan() {
        //given
        User user = User.builder()
                .username("test").password("test").enabled(1)
                .build();
        Plan plan = Plan.builder()
                .user(user).name("test").weeks(10)
                .build();
        Training training = Training.builder()
                .user(user).name("test")
                .build();
        DayName dayName = new DayName();
        PlanTraining planTraining1 = PlanTraining.builder()
                .plan(plan).training(training).week(4).dayName(dayName)
                .build();
        PlanTraining planTraining2 = PlanTraining.builder()
                .plan(plan).training(training).week(2).dayName(dayName)
                .build();
        userRepository.save(user);
        planRepository.save(plan);
        trainingRepository.save(training);
        dayNameRepository.save(dayName);
        underTest.save(planTraining1);
        underTest.save(planTraining2);
        //when
        List<PlanTraining> allTrainingsFromPlan = underTest.findAllTrainingsFromPlan(plan);
        //then
        assertThat(allTrainingsFromPlan).isNotNull();
        assertThat(allTrainingsFromPlan.size()).isEqualTo(2);
    }

    @Test
    void deleteAllFromPlan() {
        //given
        User user = User.builder()
                .username("test").password("test").enabled(1)
                .build();
        Plan plan = Plan.builder()
                .user(user).name("test").weeks(10)
                .build();
        Training training = Training.builder()
                .user(user).name("test")
                .build();
        DayName dayName = new DayName();
        PlanTraining planTraining1 = PlanTraining.builder()
                .plan(plan).training(training).week(4).dayName(dayName)
                .build();
        PlanTraining planTraining2 = PlanTraining.builder()
                .plan(plan).training(training).week(2).dayName(dayName)
                .build();
        userRepository.save(user);
        planRepository.save(plan);
        trainingRepository.save(training);
        dayNameRepository.save(dayName);
        underTest.save(planTraining1);
        underTest.save(planTraining2);
        //when
        underTest.deleteAllFromPlan(plan);
        List<PlanTraining> allTrainingsFromPlan = underTest.findAllTrainingsFromPlan(plan);
        //then
        assertThat(allTrainingsFromPlan.size()).isEqualTo(0);
    }
}