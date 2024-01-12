package pl.coderslab.twojtrening.plantraining;

import org.springframework.stereotype.Service;
import pl.coderslab.twojtrening.error.AccessUserException;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.user.User;

@Service
public class PlanTrainingService {
    private final PlanTrainingRepository planTrainingRepository;

    public PlanTrainingService(PlanTrainingRepository planTrainingRepository) {
        this.planTrainingRepository = planTrainingRepository;
    }
    public void addTrainingToPlan(PlanTraining planTraining) {planTrainingRepository.save(planTraining);
    }
    public Long deleteTrainingFromPlan(Long id, User user){
        PlanTraining planTraining = planTrainingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Training in plan with id:%s not found", id)));
        if(!planTraining.getPlan().getUser().getId().equals(user.getId())){
            throw new AccessUserException("Access forbidden");
        }
        planTrainingRepository.deleteById(planTraining.getId());
        return planTraining.getPlan().getId();
    }
    public PlanTraining getSinglePlanWithTrainingsById(Long id, User user){
        PlanTraining planTraining = planTrainingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Training in plan with id:%s not found", id)));
        if(!planTraining.getPlan().getUser().getId().equals(user.getId())){
            throw new AccessUserException("Access forbidden");
        }
        return planTraining;
    }
}
