package pl.coderslab.twojtrening.exercise;

import org.springframework.stereotype.Service;
import pl.coderslab.twojtrening.error.NotFoundException;

import javax.persistence.EntityNotFoundException;
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
    public Exercise addExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }
    public Exercise getSingleExerciseById(Long id) {
        return exerciseRepository.findById(id).orElseThrow(() -> new NotFoundException("Nie znaleziono ćwiczenia o id: " + id));
    }

    public void deleteExerciseById (Long id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        exerciseRepository.deleteById(exercise.getId());
    }

}
