package pl.coderslab.twojtrening.plantraining;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.twojtrening.dayname.DayName;
import pl.coderslab.twojtrening.dayname.DayNameService;
import pl.coderslab.twojtrening.plan.PlanService;
import pl.coderslab.twojtrening.training.TrainingService;
import pl.coderslab.twojtrening.user.CurrentUser;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PlanTrainingController {
    private final DayNameService dayNameService;
    private final PlanService planService;
    private final TrainingService trainingService;
    private final PlanTrainingService planTrainingService;


    public PlanTrainingController(DayNameService dayNameService, PlanService planService, TrainingService trainingService, PlanTrainingService planTrainingService) {
        this.dayNameService = dayNameService;
        this.planService = planService;
        this.trainingService = trainingService;
        this.planTrainingService = planTrainingService;
    }

    @ModelAttribute("days")
    public List<DayName> dayNames() {
        return dayNameService.findAllDaysNames();
    }


    @GetMapping("/plan/training/add/{id}")
    public String add(@AuthenticationPrincipal CurrentUser customUser, @PathVariable Long id, Model model) {
        PlanTraining planTraining = new PlanTraining();
        planTraining.setPlan(planService.getSinglePlanById(id, customUser.getUser()));
        model.addAttribute("trainings", trainingService.findAllTrainingsFromUser(customUser.getUser()));
        model.addAttribute("planTraining", planTraining);
        return "plantraining/add";
    }

    @PostMapping("/plan/training/add")
    public String addProcess(@Valid PlanTraining planTraining, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "plantraining/add";
        }
        planTrainingService.addTrainingToPlan(planTraining);
        return "redirect:/plan/show/" + planTraining.getPlan().getId();
    }

    @RequestMapping("/plan/training/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CurrentUser customUser) {
        Long planId = planTrainingService.deleteTrainingFromPlan(id, customUser.getUser());
        return "redirect:/plan/show/" + planId;
    }

    @GetMapping("/plan/training/edit/{id}")
    public String edit(@AuthenticationPrincipal CurrentUser customUser, @PathVariable Long id, Model model) {
        model.addAttribute("planTraining", planTrainingService.getSinglePlanWithTrainingsById(id, customUser.getUser()));
        model.addAttribute("trainings", trainingService.findAllTrainingsFromUser(customUser.getUser()));
        return "plantraining/edit";
    }

    @PostMapping("/plan/training/edit")
    public String editProcess(@Valid PlanTraining planTraining, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "plantraining/edit";
        }
        planTrainingService.addTrainingToPlan(planTraining);
        return "redirect:/plan/show/" + planTraining.getPlan().getId();
    }
}
