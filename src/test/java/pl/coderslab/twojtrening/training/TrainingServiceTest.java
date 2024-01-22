package pl.coderslab.twojtrening.training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @Mock
    TrainingRepository trainingRepository;
    @Mock
    TrainingExerciseRepository trainingExerciseRepository;
    @InjectMocks
    TrainingService underTest;

    @Test
    void shouldFindAllTrainingsFromUser() {
        //given
        User user = new User();
        user.setId(1L);
        //when
        underTest.findAllTrainingsFromUser(user);
        //then
        verify(trainingRepository).findByUser(user);
    }

    @Test
    void shouldGetSingleTrainingById() {
        //given
        long id = 1L;
        Training training = new Training();
        User user = new User();
        user.setId(id);
        training.setId(id);
        training.setUser(user);
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
        User user = new User();
        user.setId(id);
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
        Training training = new Training();
        User user = new User();
        User wrongUser = new User();
        user.setId(id);
        wrongUser.setId(2L);
        training.setId(id);
        training.setUser(user);
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
        Training training = new Training();
        training.setId(1L);
        //when
        underTest.addTraining(training);
        //then
        ArgumentCaptor<Training> trainingArgumentCaptor = ArgumentCaptor.forClass(Training.class);
        verify(trainingRepository).save(trainingArgumentCaptor.capture());
        Training capturedTraining = trainingArgumentCaptor.getValue();
        assertThat(capturedTraining).isEqualTo(training);
    }

    @Test
    void shouldDeleteTrainingById() {
        //given
        long id = 1L;
        Training training = new Training();
        User user = new User();
        user.setId(id);
        training.setId(id);
        training.setUser(user);
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
        User user = new User();
        user.setId(id);
        given(trainingRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteTrainingById(id, user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Training with id:%s not found", id));
    }
    @Test
    void shouldNotDeleteTrainingByIdWrongUser() {
        //given
        long id = 1L;
        Training training = new Training();
        User user = new User();
        User wrongUser = new User();
        user.setId(id);
        wrongUser.setId(2L);
        training.setId(id);
        training.setUser(user);
        given(trainingRepository.findById(id)).willReturn(Optional.of(training));
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteTrainingById(id, wrongUser))
                .isInstanceOf(AccessUserException.class)
                .hasMessageContaining("Access forbidden");
    }
    @Test
    void findAllExerciseFromTraining() {
        //given
        Training training = new Training();
        training.setId(1L);
        //when
        underTest.findAllExerciseFromTraining(training);
        //then
        verify(trainingExerciseRepository).findAllExercisesFromTraining(training);

    }
}