package pl.coderslab.twojtrening.exercise;

import org.springframework.stereotype.Service;
import pl.coderslab.twojtrening.error.NotFoundException;

import java.util.List;


@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public void addExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public Exercise getSingleExerciseById(Long id) {
        return exerciseRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Exercise with id:%s not found", id)));
    }

    public void deleteExerciseById(Long id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Exercise with id:%s not found", id)));
        exerciseRepository.deleteById(exercise.getId());
    }

}
