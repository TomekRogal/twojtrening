package pl.coderslab.twojtrening.plan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
    @Captor
    ArgumentCaptor<Plan> planArgumentCaptor;

    @Test
    void shouldFindAllPlansFromUser() {
        //given
        User user = User.builder().id(1L).build();
        //when
        underTest.findAllPlansFromUser(user);
        // then
        verify(planRepository).findByUser(user);
    }

    @Test
    void shouldDeletePlanById() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        Plan plan = Plan.builder()
                .id(id).user(user)
                .build();
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
        User user = User.builder().id(id).build();
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
        User user = User.builder().id(id).build();
        User wrongUser = User.builder().id(2L).build();
        Plan plan = Plan.builder()
                .id(id).user(user)
                .build();
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
        Plan plan = Plan.builder().id(1L).build();
        //when
        underTest.addPlan(plan);
        //then
        verify(planRepository).save(planArgumentCaptor.capture());
        Plan capturedPlan = planArgumentCaptor.getValue();
        assertThat(capturedPlan).isEqualTo(plan);
    }

    @Test
    void shouldGetSinglePlanById() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        Plan plan = Plan.builder()
                .id(id).user(user)
                .build();
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
        User user = User.builder().id(id).build();
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
        User user = User.builder().id(id).build();
        User wrongUser = User.builder().id(2L).build();
        Plan plan = Plan.builder()
                .id(id).user(user)
                .build();
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
        User user = User.builder().id(id).build();
        Plan plan = Plan.builder()
                .id(id).user(user)
                .build();
        Training training = Training.builder().id(id).build();
        Exercise exercise = Exercise.builder().id(id).build();
        PlanTraining planTraining = PlanTraining.builder()
                .id(id).plan(plan).training(training)
                .build();
        TrainingExercise trainingExercise = TrainingExercise.builder()
                .id(id).training(training).exercise(exercise)
                .build();
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
        User user = User.builder().id(id).build();
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
        User user = User.builder().id(id).build();
        User wrongUser = User.builder().id(2L).build();
        Plan plan = Plan.builder()
                .id(id).user(user)
                .build();
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