package pl.coderslab.twojtrening.plan;

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
class PlanRepositoryTest {
    @Autowired
    private PlanRepository underTest;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindAllPlansByUser() {
        //given
        User user = User.builder()
                .username("test").password("test").enabled(1)
                .build();
        Plan plan1 = Plan.builder()
                .user(user).name("test").weeks(10)
                .build();
        Plan plan2 = Plan.builder()
                .user(user).name("test2").weeks(5)
                .build();
        userRepository.save(user);
        underTest.save(plan1);
        underTest.save(plan2);
        //when
        List<Plan> plansByUser = underTest.findByUser(user);
        //then
        assertThat(plansByUser).isNotNull();
        assertThat(plansByUser.size()).isEqualTo(2);
    }

    @Test
    void shouldDeleteAllPlansFromUser() {
        //given
        User user = User.builder()
                .username("test").password("test").enabled(1)
                .build();
        Plan plan = Plan.builder()
                .user(user).name("test").weeks(10)
                .build();
        userRepository.save(user);
        underTest.save(plan);
        //when
        underTest.deleteAllPlansFromUser(user);
        List<Plan> plansByUser = underTest.findByUser(user);
        //then
        assertThat(plansByUser.size()).isEqualTo(0);
    }
}