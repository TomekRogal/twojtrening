package pl.coderslab.twojtrening.plan;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.twojtrening.plantraining.PlanTraining;
import pl.coderslab.twojtrening.plantraining.PlanTrainingRepository;
import pl.coderslab.twojtrening.trainingexercise.TrainingExercise;
import pl.coderslab.twojtrening.trainingexercise.TrainingExerciseRepository;
import pl.coderslab.twojtrening.user.CurrentUser;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PlanController {
    private final PlanRepository planRepository;
    private final PlanTrainingRepository planTrainingRepository;
    private final TrainingExerciseRepository trainingExerciseRepository;


    public PlanController(PlanRepository planRepository, PlanTrainingRepository planTrainingRepository, TrainingExerciseRepository trainingExerciseRepository) {
        this.planRepository = planRepository;
        this.planTrainingRepository = planTrainingRepository;
        this.trainingExerciseRepository = trainingExerciseRepository;
    }

    @RequestMapping("/plan/all")
    public String findAll(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("plans", planRepository.findByUser(customUser.getUser()));
        return "plan/all";
    }

    @RequestMapping("/plan/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CurrentUser customUser) {
        if (planRepository.findById(id).isPresent()) {
            if (planRepository.findById(id).get().getUser().getId().equals(customUser.getUser().getId())) {
                planTrainingRepository.deleteAllFromPlan(planRepository.findById(id).get());
                planRepository.deleteById(id);
                return "redirect:/plan/all";
            }
        }
        return "redirect:/plan/all";
    }

    @GetMapping("/plan/add")
    public String add(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        Plan plan = new Plan();
        plan.setUser(customUser.getUser());
        model.addAttribute("plan", plan);
        return "plan/add";
    }

    @PostMapping("/plan/add")
    public String addProcess(@AuthenticationPrincipal CurrentUser customUser, @Valid Plan plan, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "plan/add";
        }
        if(plan.getUser().getId().equals(customUser.getUser().getId())){
            planRepository.save(plan);
        }
        return "redirect:/plan/all";
    }

    @GetMapping("/plan/edit/{id}")
    public String edit(@PathVariable Long id, Model model, @AuthenticationPrincipal CurrentUser customUser) {
        if (planRepository.findById(id).isPresent()) {
            if (planRepository.findById(id).get().getUser().getId().equals(customUser.getUser().getId())) {
                model.addAttribute("plan", planRepository.findById(id).get());
                return "plan/edit";
            }
        }
        return "redirect:/plan/all";
    }

    @PostMapping("/plan/edit/{id}")
    public String editProcess(@Valid Plan plan, BindingResult bindingResult, @AuthenticationPrincipal CurrentUser customUser) {
        if (bindingResult.hasErrors()) {
            return "plan/edit";
        }
        if(plan.getUser().getId().equals(customUser.getUser().getId())) {
            planRepository.save(plan);
        }
        return "redirect:/plan/all";
    }

    @GetMapping("/plan/show/{id}")
    public String show(@PathVariable Long id, Model model, @AuthenticationPrincipal CurrentUser customUser) {
        if (planRepository.findById(id).isPresent()) {
            if (planRepository.findById(id).get().getUser().getId().equals(customUser.getUser().getId())) {
                model.addAttribute("plan", planRepository.findById(id).get());
                Map<PlanTraining, List<TrainingExercise>> te = new LinkedHashMap<>();
                List<PlanTraining> allTrainingsFromPlan = planTrainingRepository.findAllTrainingsFromPlan(planRepository.findById(id).get());
                allTrainingsFromPlan.forEach(e -> te.put(e, trainingExerciseRepository.findAllExercisesFromTraining(e.getTraining())));
                model.addAttribute("trainingsList", te);
                return "plan/show";
            }
        }
        return "redirect:/plan/all";
    }
}
