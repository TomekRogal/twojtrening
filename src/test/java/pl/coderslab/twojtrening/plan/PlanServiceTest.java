package pl.coderslab.twojtrening.plan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.twojtrening.plantraining.PlanTrainingRepository;
import pl.coderslab.twojtrening.trainingexercise.TrainingExerciseRepository;
import pl.coderslab.twojtrening.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class PlanServiceTest {
    @Mock PlanRepository planRepository;
    @Mock PlanTrainingRepository planTrainingRepository;
    @Mock TrainingExerciseRepository trainingExerciseRepository;
    @InjectMocks PlanService underTest;

    @Test
    void shouldFindAllPlansFromUser() {
        //given
        User user = new User();
        user.setId(1L);
        //when
        underTest.findAllPlansFromUser(user);
        // then
        verify(planRepository).findByUser(user);
    }

    @Test
    void deletePlanById() {
    }

    @Test
    void shouldAddNewPlan() {
        //given
        Plan plan = new Plan();
        plan.setId(1L);
        //when
        underTest.addPlan(plan);
        //then
        ArgumentCaptor<Plan> planArgumentCaptor = ArgumentCaptor.forClass(Plan.class);
        verify(planRepository).save(planArgumentCaptor.capture());
        Plan capturedPlan = planArgumentCaptor.getValue();
        assertThat(capturedPlan).isEqualTo(plan);
    }

    @Test
    void getSinglePlanById() {
    }

    @Test
    void getSinglePlanWithTrainingsAndExercisesById() {
    }
}