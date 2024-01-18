package pl.coderslab.twojtrening.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    ExerciseRepository exerciseRepository;
    private ExerciseService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ExerciseService(exerciseRepository);
    }

    @Test
    void findAllExercises() {
        //when
        underTest.findAllExercises();
        //then
        verify(exerciseRepository).findAll();
    }

    @Test
    void addExercise() {
    }

    @Test
    void getSingleExerciseById() {
    }

    @Test
    void deleteExerciseById() {
    }
}