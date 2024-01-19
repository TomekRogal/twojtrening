package pl.coderslab.twojtrening.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        long id = 1L;
        Exercise exercise = new Exercise();
        exercise.setName("test");
        exercise.setDescription("test");

        when(exerciseRepository.findById(id)).thenReturn(Optional.of(exercise));
        underTest.getSingleExerciseById(id);

        verify(exerciseRepository).findById(id);

    }

    @Test
    void deleteExerciseById() {
    }
}