package pl.coderslab.twojtrening.training;

import org.springframework.stereotype.Service;
import pl.coderslab.twojtrening.error.AccessUserException;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.trainingexercise.TrainingExercise;
import pl.coderslab.twojtrening.trainingexercise.TrainingExerciseRepository;
import pl.coderslab.twojtrening.user.User;

import java.util.List;

@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingExerciseRepository trainingExerciseRepository;

    public TrainingService(TrainingRepository trainingRepository, TrainingExerciseRepository trainingExerciseRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingExerciseRepository = trainingExerciseRepository;
    }


    public List<Training> findAllTrainingsFromUser(User user) {
        return trainingRepository.findByUser(user);
    }
    public Training getSingleTrainingById(Long id, User user) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Plan with id:%s not found", id)));
        if(!training.getUser().getId().equals(user.getId())){
            throw new AccessUserException("Brak dostępu");
        }
        return training;
    }
    public void addTraining(Training training){trainingRepository.save(training);}
    public void deleteTrainingById(Long id, User user) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Plan with id:%s not found", id)));
        if(!training.getUser().getId().equals(user.getId())){
            throw new AccessUserException("Access forbidden");
        }
        trainingExerciseRepository.deleteAllFromTraining(training);
        trainingRepository.deleteById(training.getId());
    }
    public List<TrainingExercise> findAllExerciseFromTraining(Training training){
        return trainingExerciseRepository.findAllExercisesFromTraining(training);
    }



}
