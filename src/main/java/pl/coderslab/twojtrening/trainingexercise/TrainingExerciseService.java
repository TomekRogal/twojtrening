package pl.coderslab.twojtrening.trainingexercise;

import org.springframework.stereotype.Service;
import pl.coderslab.twojtrening.error.AccessUserException;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.user.User;

import java.util.List;

@Service
public class TrainingExerciseService {
    private final TrainingExerciseRepository trainingExerciseRepository;

    public TrainingExerciseService(TrainingExerciseRepository trainingExerciseRepository) {
        this.trainingExerciseRepository = trainingExerciseRepository;
    }

    public void addExerciseToTraining(TrainingExercise trainingExercise){
        trainingExerciseRepository.save(trainingExercise);
    }
    public TrainingExercise getSingleTrainingWithExercisesById(Long id, User user){
        TrainingExercise trainingExercise = trainingExerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Exercise in training with id:%s not found", id)));
        if(!trainingExercise.getTraining().getUser().getId().equals(user.getId())){
            throw new AccessUserException("Access forbidden");
        }
        return trainingExercise;
    }
    public Long deleteExerciseFromTraining(Long id, User user){
        TrainingExercise trainingExercise = trainingExerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Exercise in training with id:%s not found", id)));
        if(!trainingExercise.getTraining().getUser().getId().equals(user.getId())){
            throw new AccessUserException("Access forbidden");
        }
        trainingExerciseRepository.deleteById(trainingExercise.getId());
        return trainingExercise.getTraining().getId();
    }
}
