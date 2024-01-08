package pl.coderslab.twojtrening.plantraining;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.twojtrening.dayname.DayName;
import pl.coderslab.twojtrening.dayname.DayNameRepository;
import pl.coderslab.twojtrening.plan.Plan;
import pl.coderslab.twojtrening.plan.PlanRepository;
import pl.coderslab.twojtrening.training.TrainingRepository;
import pl.coderslab.twojtrening.user.CurrentUser;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PlanTrainingController {
    private final PlanTrainingRepository planTrainingRepository;
    private final TrainingRepository trainingRepository;
    private final DayNameRepository dayNameRepository;
    private final PlanRepository planRepository;


    public PlanTrainingController(PlanTrainingRepository planTrainingRepository, TrainingRepository trainingRepository, DayNameRepository dayNameRepository, PlanRepository planRepository) {
        this.planTrainingRepository = planTrainingRepository;
        this.trainingRepository = trainingRepository;
        this.dayNameRepository = dayNameRepository;
        this.planRepository = planRepository;
    }

    @ModelAttribute("days")
    public List<DayName> dayNames() {
        return dayNameRepository.findAll();
    }

    @ModelAttribute("plans")
    public List<Plan> plans() {
        return planRepository.findAll();
    }

    @GetMapping("/plan/training/add/{id}")
    public String add(@AuthenticationPrincipal CurrentUser customUser, @PathVariable Long id, Model model) {
        if (planRepository.findById(id).isPresent()) {
            if (planRepository.findById(id).get().getUser().getId().equals(customUser.getUser().getId())) {
                PlanTraining planTraining = new PlanTraining();
                planTraining.setPlan(planRepository.findById(id).get());
                model.addAttribute("trainings", trainingRepository.findByUser(customUser.getUser()));
                model.addAttribute("planTraining", planTraining);
                return "plantraining/add";
            }
        }
        return "redirect:/plan/all";
    }

    @PostMapping("/plan/training/add")
    public String addProcess(@Valid PlanTraining planTraining, BindingResult bindingResult, @AuthenticationPrincipal CurrentUser customUser) {
        if (bindingResult.hasErrors()) {
            return "plantraining/add";
        }
        if (planTraining.getPlan().getUser().getId().equals(customUser.getUser().getId()) && planTraining.getTraining().getUser().getId().equals(customUser.getUser().getId())) {
            planTrainingRepository.save(planTraining);
        }
        return "redirect:/plan/show/" + planTraining.getPlan().getId();
    }

    @RequestMapping("/plan/training/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CurrentUser customUser) {
        if (planTrainingRepository.findById(id).isPresent()) {
            if (planTrainingRepository.findById(id).get().getPlan().getUser().getId().equals(customUser.getUser().getId())) {
                PlanTraining planTraining = planTrainingRepository.findById(id).get();
                planTrainingRepository.deleteById(id);
                return "redirect:/plan/show/" + planTraining.getPlan().getId();
            }
        }
        return "redirect:/plan/all";
    }

    @GetMapping("/plan/training/edit/{id}")
    public String edit(@AuthenticationPrincipal CurrentUser customUser, @PathVariable Long id, Model model) {
        if (planTrainingRepository.findById(id).isPresent()) {
            if (planTrainingRepository.findById(id).get().getPlan().getUser().getId().equals(customUser.getUser().getId())) {
                model.addAttribute("planTraining", planTrainingRepository.findById(id).get());
                model.addAttribute("trainings", trainingRepository.findByUser(customUser.getUser()));
                return "plantraining/edit";
            }
        }
        return "redirect:/plan/all";
    }

    @PostMapping("/plan/training/edit")
    public String editProcess(@Valid PlanTraining planTraining, BindingResult bindingResult, @AuthenticationPrincipal CurrentUser customUser) {
        if (bindingResult.hasErrors()) {
            return "plantraining/edit";
        }
        if (planTraining.getPlan().getUser().getId().equals(customUser.getUser().getId()) && planTraining.getTraining().getUser().getId().equals(customUser.getUser().getId())) {
            planTrainingRepository.save(planTraining);
        }

        return "redirect:/plan/show/" + planTraining.getPlan().getId();
    }
}
