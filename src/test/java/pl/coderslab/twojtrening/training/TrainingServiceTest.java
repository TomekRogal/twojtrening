package pl.coderslab.twojtrening.training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.twojtrening.error.AccessUserException;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.trainingexercise.TrainingExerciseRepository;
import pl.coderslab.twojtrening.user.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @Mock
    TrainingRepository trainingRepository;
    @Mock
    TrainingExerciseRepository trainingExerciseRepository;
    @InjectMocks
    TrainingService underTest;
    @Captor
    ArgumentCaptor<Training> trainingArgumentCaptor;
    @Test
    void shouldFindAllTrainingsFromUser() {
        //given
        User user = User.builder().id(1L).build();
        //when
        underTest.findAllTrainingsFromUser(user);
        //then
        verify(trainingRepository).findByUser(user);
    }

    @Test
    void shouldGetSingleTrainingById() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        Training training = Training.builder()
                .id(id).user(user)
                .build();
        given(trainingRepository.findById(id)).willReturn(Optional.of(training));
        //when
        Training singleTrainingById = underTest.getSingleTrainingById(id, user);
        //then
        verify(trainingRepository).findById(id);
        assertThat(singleTrainingById).isEqualTo(training);
    }

    @Test
    void shouldNotGetSingleTrainingByIdWrongId() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        given(trainingRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.getSingleTrainingById(id, user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Training with id:%s not found", id));
    }

    @Test
    void shouldNotGetSingleTrainingByIdWrongUser() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        Training training = Training.builder()
                .id(id).user(user)
                .build();
        User wrongUser = User.builder().id(2L).build();
        given(trainingRepository.findById(id)).willReturn(Optional.of(training));
        //when
        //then
        assertThatThrownBy(() -> underTest.getSingleTrainingById(id, wrongUser))
                .isInstanceOf(AccessUserException.class)
                .hasMessageContaining("Access forbidden");
    }

    @Test
    void shouldAddNewTraining() {
        //given
        Training training = Training.builder().id(1L).build();
        //when
        underTest.addTraining(training);
        //then
        verify(trainingRepository).save(trainingArgumentCaptor.capture());
        Training capturedTraining = trainingArgumentCaptor.getValue();
        assertThat(capturedTraining).isEqualTo(training);
    }

    @Test
    void shouldDeleteTrainingById() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        Training training = Training.builder()
                .id(id).user(user)
                .build();
        given(trainingRepository.findById(id)).willReturn(Optional.of(training));
        //when
        underTest.deleteTrainingById(id, user);
        //then
        verify(trainingRepository).findById(id);
        verify(trainingExerciseRepository).deleteAllFromTraining(training);
        verify(trainingRepository).deleteById(id);
    }
    @Test
    void shouldNotDeleteTrainingByIdWrongId() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        given(trainingRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteTrainingById(id, user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Training with id:%s not found", id));
        verify(trainingExerciseRepository,never()).deleteAllFromTraining(any());
        verify(trainingRepository,never()).deleteById(any());
    }
    @Test
    void shouldNotDeleteTrainingByIdWrongUser() {
        //given
        long id = 1L;
        User user = User.builder().id(id).build();
        Training training = Training.builder()
                .id(id).user(user)
                .build();
        User wrongUser = User.builder().id(2L).build();
        given(trainingRepository.findById(id)).willReturn(Optional.of(training));
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteTrainingById(id, wrongUser))
                .isInstanceOf(AccessUserException.class)
                .hasMessageContaining("Access forbidden");
        verify(trainingExerciseRepository,never()).deleteAllFromTraining(any());
        verify(trainingRepository,never()).deleteById(any());
    }
    @Test
    void findAllExerciseFromTraining() {
        //given
        Training training = Training.builder().id(1L).build();
        //when
        underTest.findAllExerciseFromTraining(training);
        //then
        verify(trainingExerciseRepository).findAllExercisesFromTraining(training);

    }
}