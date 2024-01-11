package pl.coderslab.twojtrening.training;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.twojtrening.trainingexercise.TrainingExerciseRepository;
import pl.coderslab.twojtrening.user.CurrentUser;

import javax.validation.Valid;


@Controller
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @RequestMapping("/training/all")
    public String findAll(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("trainings", trainingService.findAllTrainingsFromUser(customUser.getUser()));
        return "training/all";
    }

    @RequestMapping("/training/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CurrentUser customUser) {
        trainingService.deleteTrainingById(id, customUser.getUser());
        return "redirect:/training/all";
    }


    @GetMapping("/training/add")
    public String add(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        Training training = new Training();
        training.setUser(customUser.getUser());
        model.addAttribute("training", training);
        return "training/add";
    }

    @PostMapping("/training/add")
    public String addProcess(@Valid Training training, BindingResult bindingResult, @AuthenticationPrincipal CurrentUser customUser) {
        if (bindingResult.hasErrors()) {
            return "training/add";
        }
         trainingService.addTraining(training);
        return "redirect:/training/all";
    }

    @GetMapping("/training/edit/{id}")
    public String edit(@AuthenticationPrincipal CurrentUser customUser, @PathVariable Long id, Model model) {
                model.addAttribute("training", trainingService.getSingleTrainingById(id,customUser.getUser()));
                return "training/edit";

    }

    @PostMapping("/training/edit/{id}")
    public String editProcess(@Valid Training training, BindingResult bindingResult, @AuthenticationPrincipal CurrentUser customUser) {
        if (bindingResult.hasErrors()) {
            return "training/edit";
        }
        trainingService.addTraining(training);
        return "redirect:/training/all";
    }

    @GetMapping("/training/show/{id}")
    public String show(@PathVariable Long id, Model model, @AuthenticationPrincipal CurrentUser customUser) {
        Training training = trainingService.getSingleTrainingById(id,customUser.getUser());
        model.addAttribute("training", training);
        model.addAttribute("exercises", trainingService.findAllExerciseFromTraining(training));
        return "training/show";
    }

}
