package pl.coderslab.twojtrening.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.twojtrening.error.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock ExerciseRepository exerciseRepository;
    private ExerciseService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ExerciseService(exerciseRepository);
    }

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
        exercise.setName("test");
        exercise.setDescription("test");
        //when
        underTest.addExercise(exercise);
        //then
        ArgumentCaptor<Exercise> exerciseArgumentCaptor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(exerciseArgumentCaptor.capture());
        Exercise capturedExercise = exerciseArgumentCaptor.getValue();
        assertThat(capturedExercise).isEqualTo(exercise);
    }

    @Test
    void shouldGetSingleExerciseById() {
        //given
        long id = 1L;
        Exercise exercise = new Exercise();
        exercise.setName("test");
        exercise.setDescription("test");
        given(exerciseRepository.findById(id)).willReturn(Optional.of(exercise));
        //when
        underTest.getSingleExerciseById(id);
        //then
        verify(exerciseRepository).findById(id);

    }
    @Test
    void shouldNotGetSingleExerciseById() {
        //given
        long id = 1L;
        given(exerciseRepository.findById(id)).willReturn(Optional.empty());
        //when

        //then
        assertThatThrownBy(()-> underTest.getSingleExerciseById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Exercise with id:%s not found", id));

    }

    @Test
    void shouldDeleteExerciseById() {
        //given
        long id = 1L;
        Exercise exercise = new Exercise();
        exercise.setName("test");
        exercise.setDescription("test");
        given(exerciseRepository.findById(id)).willReturn(Optional.of(exercise));
        //when
        //then
        assertAll(()->underTest.deleteExerciseById(id));
    }
    @Test
    void shouldNotDeleteExerciseById() {
        //given
        long id = 1L;
        Exercise exercise = new Exercise();
        exercise.setName("test");
        exercise.setDescription("test");
        given(exerciseRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(()-> underTest.deleteExerciseById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Exercise with id:%s not found", id));
    }
}