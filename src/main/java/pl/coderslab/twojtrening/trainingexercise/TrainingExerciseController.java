package pl.coderslab.twojtrening.trainingexercise;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.twojtrening.exercise.Exercise;
import pl.coderslab.twojtrening.exercise.ExerciseRepository;
import pl.coderslab.twojtrening.exercise.ExerciseService;
import pl.coderslab.twojtrening.training.Training;
import pl.coderslab.twojtrening.training.TrainingRepository;
import pl.coderslab.twojtrening.training.TrainingService;
import pl.coderslab.twojtrening.user.CurrentUser;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TrainingExerciseController {
    private final TrainingService trainingService;
    private final TrainingExerciseService trainingExerciseService;
    private final ExerciseService exerciseService;

    public TrainingExerciseController(TrainingService trainingService, TrainingExerciseService trainingExerciseService, ExerciseService exerciseService) {
        this.trainingService = trainingService;
        this.trainingExerciseService = trainingExerciseService;
        this.exerciseService = exerciseService;
    }

    @ModelAttribute("exercises")
    public List<Exercise> exercises() {
        return exerciseService.findAllExercises();
    }

    @GetMapping("/training/exercise/add/{id}")
    public String add(@PathVariable Long id, Model model, @AuthenticationPrincipal CurrentUser customUser) {
        TrainingExercise trainingExercise = new TrainingExercise();
        Training training = trainingService.getSingleTrainingById(id, customUser.getUser());
        trainingExercise.setTraining(training);
        model.addAttribute("trainingExercise", trainingExercise);
        return "trainingexercise/add";
    }

    @PostMapping("/training/exercise/add")
    public String addProcess(@Valid TrainingExercise trainingExercise, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "trainingexercise/add";
        }
        trainingExerciseService.addExerciseToTraining(trainingExercise);
        return "redirect:/training/show/" + trainingExercise.getTraining().getId();
    }

    @RequestMapping("/training/exercise/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CurrentUser customUser) {
        Long trainingId = trainingExerciseService.deleteExerciseFromTraining(id, customUser.getUser());
        return "redirect:/training/show/" + trainingId;

    }

    @GetMapping("/training/exercise/edit/{id}")
    public String edit(@PathVariable Long id, Model model, @AuthenticationPrincipal CurrentUser customUser) {
        model.addAttribute("trainingExercise", trainingExerciseService.getSingleTrainingWithExercisesById(id, customUser.getUser()));
        return "trainingexercise/edit";
    }

    @PostMapping("/training/exercise/edit")
    public String editProcess(@Valid TrainingExercise trainingExercise, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "trainingexercise/edit";
        }
        trainingExerciseService.addExerciseToTraining(trainingExercise);
        return "redirect:/training/show/" + trainingExercise.getTraining().getId();
    }

    @GetMapping("/training/exercise/addex/{id}")
    public String addex(@PathVariable Long id, Model model, @AuthenticationPrincipal CurrentUser customUser) {
        TrainingExercise trainingExercise = new TrainingExercise();
        trainingExercise.setExercise(exerciseService.getSingleExerciseById(id));
        model.addAttribute("trainings", trainingService.findAllTrainingsFromUser(customUser.getUser()));
        model.addAttribute("trainingExercise", trainingExercise);
        return "trainingexercise/addex";

    }

    @PostMapping("/training/exercise/addex")
    public String addexProcess(@Valid TrainingExercise trainingExercise, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "trainingexercise/addex";
        }
            trainingExerciseService.addExerciseToTraining(trainingExercise);
        return "redirect:/exercise/all";
    }
}
