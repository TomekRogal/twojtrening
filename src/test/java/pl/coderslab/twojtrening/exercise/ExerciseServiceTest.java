package pl.coderslab.twojtrening.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.twojtrening.error.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    ExerciseRepository exerciseRepository;
    @InjectMocks
    private ExerciseService underTest;
    @Captor
    ArgumentCaptor<Exercise> exerciseArgumentCaptor;



    @Test
    void shouldFindAllExercises() {
        //when
        underTest.findAllExercises();
        //then
        verify(exerciseRepository).findAll();
    }

    @Test
    void shouldAddNewExercise() {
        //given
        Exercise exercise = new Exercise();
        //when
        underTest.addExercise(exercise);
        //then
        verify(exerciseRepository).save(exerciseArgumentCaptor.capture());
        Exercise capturedExercise = exerciseArgumentCaptor.getValue();
        assertThat(capturedExercise).isEqualTo(exercise);
    }

    @Test
    void shouldGetSingleExerciseById() {
        //given
        long id = 1L;
        Exercise exercise = Exercise.builder().id(id).build();
        given(exerciseRepository.findById(id)).willReturn(Optional.of(exercise));
        //when
        Exercise singleExerciseById = underTest.getSingleExerciseById(id);
        //then
        verify(exerciseRepository).findById(id);
        assertThat(singleExerciseById).isEqualTo(exercise);

    }

    @Test
    void shouldNotGetSingleExerciseById() {
        //given
        long id = 1L;
        given(exerciseRepository.findById(id)).willReturn(Optional.empty());
        //when

        //then
        assertThatThrownBy(() -> underTest.getSingleExerciseById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Exercise with id:%s not found", id));

    }

    @Test
    void shouldDeleteExerciseById() {
        //given
        long id = 1L;
        Exercise exercise = Exercise.builder().id(id).build();
        given(exerciseRepository.findById(id)).willReturn(Optional.of(exercise));
        //when
        underTest.deleteExerciseById(id);
        //then
        verify(exerciseRepository).findById(id);
        verify(exerciseRepository).deleteById(id);
    }

    @Test
    void shouldNotDeleteExerciseById() {
        //given
        long id = 1L;
        given(exerciseRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteExerciseById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Exercise with id:%s not found", id));
        verify(exerciseRepository, never()).deleteById(any());
    }
}