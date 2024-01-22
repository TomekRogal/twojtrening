package pl.coderslab.twojtrening.plan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.twojtrening.error.AccessUserException;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.exercise.Exercise;
import pl.coderslab.twojtrening.plantraining.PlanTraining;
import pl.coderslab.twojtrening.plantraining.PlanTrainingRepository;
import pl.coderslab.twojtrening.training.Training;
import pl.coderslab.twojtrening.trainingexercise.TrainingExercise;
import pl.coderslab.twojtrening.trainingexercise.TrainingExerciseRepository;
import pl.coderslab.twojtrening.user.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class PlanServiceTest {
    @Mock
    PlanRepository planRepository;
    @Mock
    PlanTrainingRepository planTrainingRepository;
    @Mock
    TrainingExerciseRepository trainingExerciseRepository;
    @InjectMocks
    PlanService underTest;

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
    void shouldDeletePlanById() {
        //given
        long id = 1L;
        Plan plan = new Plan();
        User user = new User();
        user.setId(id);
        plan.setId(id);
        plan.setUser(user);
        given(planRepository.findById(id)).willReturn(Optional.of(plan));
        //when
        underTest.deletePlanById(id, user);
        //then
        verify(planRepository).findById(id);
        verify(planTrainingRepository).deleteAllFromPlan(plan);
        verify(planRepository).deleteById(id);
    }
    @Test
    void shouldNotDeletePlanByIdWrongId() {
        //given
        long id = 1L;
        User user = new User();
        user.setId(id);
        given(planRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.deletePlanById(id, user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Plan with id:%s not found", id));
        verify(planRepository,never()).deleteById(any());
        verify(planTrainingRepository,never()).deleteAllFromPlan(any());
    }
    @Test
    void shouldNotDeletePlanByIdWrongUser() {
        //given
        long id = 1L;
        Plan plan = new Plan();
        User user = new User();
        User wrongUser = new User();
        user.setId(id);
        wrongUser.setId(2L);
        plan.setId(id);
        plan.setUser(user);
        given(planRepository.findById(id)).willReturn(Optional.of(plan));
        //when
        //then
        assertThatThrownBy(() -> underTest.deletePlanById(id, wrongUser))
                .isInstanceOf(AccessUserException.class)
                .hasMessageContaining("Access forbidden");
        verify(planRepository,never()).deleteById(any());
        verify(planTrainingRepository,never()).deleteAllFromPlan(any());
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
        User user = new User();
        user.setId(id);
        given(planRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.getSinglePlanById(id, user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Plan with id:%s not found", id));

    }

    @Test
    void shouldNotGetSinglePlanByIdWrongUser() {
        //given
        long id = 1L;
        Plan plan = new Plan();
        User user = new User();
        User wrongUser = new User();
        user.setId(id);
        wrongUser.setId(2L);
        plan.setId(id);
        plan.setUser(user);
        given(planRepository.findById(id)).willReturn(Optional.of(plan));
        //when
        //then
        assertThatThrownBy(() -> underTest.getSinglePlanById(id, wrongUser))
                .isInstanceOf(AccessUserException.class)
                .hasMessageContaining("Access forbidden");

    }

    @Test
    void shouldGetSinglePlanWithTrainingsAndExercisesById() {
        long id = 1L;
        Plan plan = new Plan();
        User user = new User();
        Training training = new Training();
        Exercise exercise = new Exercise();
        PlanTraining planTraining = new PlanTraining();
        TrainingExercise trainingExercise = new TrainingExercise();
        user.setId(id);
        plan.setId(id);
        plan.setUser(user);
        training.setId(id);
        exercise.setId(id);
        planTraining.setId(id);
        planTraining.setPlan(plan);
        planTraining.setTraining(training);
        trainingExercise.setId(id);
        trainingExercise.setTraining(training);
        trainingExercise.setExercise(exercise);
        Map<PlanTraining, List<TrainingExercise>> expectedResult = Map.of(planTraining,List.of(trainingExercise));
        given(planRepository.findById(id)).willReturn(Optional.of(plan));
        given(planTrainingRepository.findAllTrainingsFromPlan(plan)).willReturn(List.of(planTraining));
        given(trainingExerciseRepository.findAllExercisesFromTraining(training)).willReturn(List.of(trainingExercise));
        //when
        Map<PlanTraining, List<TrainingExercise>> singlePlanWithTrainingsAndExercisesById = underTest.getSinglePlanWithTrainingsAndExercisesById(id, user);
        //then
        verify(planRepository).findById(id);
        verify(planTrainingRepository).findAllTrainingsFromPlan(plan);
        verify(trainingExerciseRepository).findAllExercisesFromTraining(training);
        assertThat(singlePlanWithTrainingsAndExercisesById).isEqualTo(expectedResult);
    }
    @Test
    void shouldNotGetSinglePlanWithTrainingsAndExercisesByIdWrongId() {
        //given
        long id = 1L;
        User user = new User();
        user.setId(id);
        given(planRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.getSinglePlanWithTrainingsAndExercisesById(id, user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Plan with id:%s not found", id));
        verify(planTrainingRepository,never()).findAllTrainingsFromPlan(any());
        verify(trainingExerciseRepository,never()).findAllExercisesFromTraining(any());
    }
    @Test
    void shouldNotGetSinglePlanWithTrainingsAndExercisesByIdWrongUser() {
        long id = 1L;
        Plan plan = new Plan();
        User user = new User();
        User wrongUser = new User();
        user.setId(id);
        wrongUser.setId(2L);
        plan.setId(id);
        plan.setUser(user);
        given(planRepository.findById(id)).willReturn(Optional.of(plan));
        //when
        //then
        assertThatThrownBy(() -> underTest.getSinglePlanWithTrainingsAndExercisesById(id, wrongUser))
                .isInstanceOf(AccessUserException.class)
                .hasMessageContaining("Access forbidden");
        verify(planTrainingRepository,never()).findAllTrainingsFromPlan(any());
        verify(trainingExerciseRepository,never()).findAllExercisesFromTraining(any());

    }
}