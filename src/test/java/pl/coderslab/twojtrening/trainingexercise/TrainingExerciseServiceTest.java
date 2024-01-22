package pl.coderslab.twojtrening.trainingexercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.twojtrening.error.AccessUserException;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.training.Training;
import pl.coderslab.twojtrening.user.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingExerciseServiceTest {
    @Mock
    TrainingExerciseRepository trainingExerciseRepository;
    @InjectMocks
    TrainingExerciseService underTest;
    @Captor
    ArgumentCaptor<TrainingExercise> trainingExerciseArgumentCaptor;

    @Test
    void shouldAddExerciseToTraining() {
        //given
        TrainingExercise trainingExercise = TrainingExercise.builder().id(1L).build();
        //when
        underTest.addExerciseToTraining(trainingExercise);
        //then
        verify(trainingExerciseRepository).save(trainingExerciseArgumentCaptor.capture());
        TrainingExercise capturedTrainingExercise = trainingExerciseArgumentCaptor.getValue();
        assertThat(capturedTrainingExercise).isEqualTo(trainingExercise);
    }

    @Test
    void shouldGetSingleTrainingWithExercisesById() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        Training training = Training.builder()
                .id(id).user(user)
                .build();
        TrainingExercise trainingExercise = TrainingExercise.builder()
                .id(id).training(training)
                .build();
        given(trainingExerciseRepository.findById(id)).willReturn(Optional.of(trainingExercise));
        //when
        TrainingExercise singleTrainingWithExercisesById = underTest.getSingleTrainingWithExercisesById(id, user);
        //then
        verify(trainingExerciseRepository).findById(id);
        assertThat(singleTrainingWithExercisesById).isEqualTo(trainingExercise);
    }
    @Test
    void shouldNotGetSingleTrainingWithExercisesByIdWrongId() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        given(trainingExerciseRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.getSingleTrainingWithExercisesById(id, user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Exercise in training with id:%s not found", id));
    }
    @Test
    void shouldNotGetSingleTrainingWithExercisesByIdWrongUser() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        Training training = Training.builder()
                .id(id).user(user)
                .build();
        User wrongUser = User.builder().id(2L).build();
        TrainingExercise trainingExercise = TrainingExercise.builder()
                .id(id).training(training)
                .build();
        given(trainingExerciseRepository.findById(id)).willReturn(Optional.of(trainingExercise));
        //when
        //then
        assertThatThrownBy(() -> underTest.getSingleTrainingWithExercisesById(id, wrongUser))
                .isInstanceOf(AccessUserException.class)
                .hasMessageContaining("Access forbidden");
    }
    @Test
    void shouldDeleteExerciseFromTraining() {
        long id = 1L;
        User user = User.builder().id(id).build();
        Training training = Training.builder()
                .id(id).user(user)
                .build();
        TrainingExercise trainingExercise = TrainingExercise.builder()
                .id(id).training(training)
                .build();
        given(trainingExerciseRepository.findById(id)).willReturn(Optional.of(trainingExercise));
        //when
        underTest.deleteExerciseFromTraining(id, user);
        //then
        verify(trainingExerciseRepository).findById(id);
        verify(trainingExerciseRepository).deleteById(id);
    }
    @Test
    void shouldNotDeleteExerciseFromTrainingWrongId() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        given(trainingExerciseRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteExerciseFromTraining(id, user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Exercise in training with id:%s not found", id));

        verify(trainingExerciseRepository,never()).deleteById(any());
    }
    @Test
    void shouldNotDeleteExerciseFromTrainingWrongUser() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        Training training = Training.builder()
                .id(id).user(user)
                .build();
        User wrongUser = User.builder().id(2L).build();
        TrainingExercise trainingExercise = TrainingExercise.builder()
                .id(id).training(training)
                .build();
        given(trainingExerciseRepository.findById(id)).willReturn(Optional.of(trainingExercise));
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteExerciseFromTraining(id, wrongUser))
                .isInstanceOf(AccessUserException.class)
                .hasMessageContaining("Access forbidden");
        verify(trainingExerciseRepository,never()).deleteById(any());
    }
}