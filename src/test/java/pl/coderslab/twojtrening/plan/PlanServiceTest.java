package pl.coderslab.twojtrening.plan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.twojtrening.error.AccessUserException;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.plantraining.PlanTrainingRepository;
import pl.coderslab.twojtrening.trainingexercise.TrainingExerciseRepository;
import pl.coderslab.twojtrening.user.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
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
    void shouldGetSinglePlanById() {
        //given
        long id = 1L;
        Plan plan = new Plan();
        User user = new User();
        user.setId(id);
        plan.setId(id);
        plan.setUser(user);
        given(planRepository.findById(id)).willReturn(Optional.of(plan));
        //when
        Plan singlePlanById = underTest.getSinglePlanById(id, user);
        //then
        verify(planRepository).findById(id);
        assertThat(singlePlanById).isEqualTo(plan);
    }
    @Test
    void shouldNotGetSinglePlanByIdWrongId() {
        //given
        long id = 1L;
        Plan plan = new Plan();
        User user = new User();
        user.setId(id);
        User wrongUser = new User();
        wrongUser.setId(2L);
        plan.setId(id);
        plan.setUser(user);
        given(planRepository.findById(id)).willReturn(Optional.of(plan));
        //when
        //then
        assertThatThrownBy(() -> underTest.getSinglePlanById(id,wrongUser))
                .isInstanceOf(AccessUserException.class)
                .hasMessageContaining("Access forbidden");

    }
    @Test
    void shouldNotGetSinglePlanByIdWrongUser() {
        //given
        long id = 1L;
        User user = new User();
        user.setId(id);
        given(planRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.getSinglePlanById(id,user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Plan with id:%s not found", id));

    }

    @Test
    void getSinglePlanWithTrainingsAndExercisesById() {
    }
}