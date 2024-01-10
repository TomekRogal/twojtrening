package pl.coderslab.twojtrening.plan;

import org.springframework.stereotype.Service;
import pl.coderslab.twojtrening.error.AccessUserException;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.plantraining.PlanTraining;
import pl.coderslab.twojtrening.plantraining.PlanTrainingRepository;
import pl.coderslab.twojtrening.trainingexercise.TrainingExercise;
import pl.coderslab.twojtrening.trainingexercise.TrainingExerciseRepository;
import pl.coderslab.twojtrening.user.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanTrainingRepository planTrainingRepository;
    private final TrainingExerciseRepository trainingExerciseRepository;

    public PlanService(PlanRepository planRepository, PlanTrainingRepository planTrainingRepository, TrainingExerciseRepository trainingExerciseRepository) {
        this.planRepository = planRepository;
        this.planTrainingRepository = planTrainingRepository;
        this.trainingExerciseRepository = trainingExerciseRepository;
    }

    public List<Plan> findAllPlansFromUser(User user) {
        return planRepository.findByUser(user);
    }

    public void deletePlanById(Long id, User user) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Plan with id:%s not found", id)));
        if(!plan.getUser().getId().equals(user.getId())){
            throw new AccessUserException("Access forbidden");
        }
        planTrainingRepository.deleteAllFromPlan(plan);
        planRepository.deleteById(plan.getId());
    }

    public void addPlan(Plan plan) {
        planRepository.save(plan);
    }

    public Plan getSinglePlanById(Long id, User user) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Plan with id:%s not found", id)));
        if(!plan.getUser().getId().equals(user.getId())){
            throw new AccessUserException("Brak dostępu");
        }
        return plan;
    }
    public Map<PlanTraining, List<TrainingExercise>> getSinglePlanWithTrainingsAndExercisesById (Long id, User user) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Plan with id:%s not found", id)));
        if(!plan.getUser().getId().equals(user.getId())){
            throw new AccessUserException("Brak dostępu");
        }
        Map<PlanTraining, List<TrainingExercise>> planTrainingListLinkedHashMap = new LinkedHashMap<>();
        planTrainingRepository.findAllTrainingsFromPlan(plan)
                .forEach(planTraining -> planTrainingListLinkedHashMap
                .put(planTraining, trainingExerciseRepository
                        .findAllExercisesFromTraining(planTraining.getTraining())));
        return planTrainingListLinkedHashMap;
    }
}

