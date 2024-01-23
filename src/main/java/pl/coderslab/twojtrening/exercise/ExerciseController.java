package pl.coderslab.twojtrening.exercise;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @RequestMapping("/exercise/all")
    public String findAll(Model model) {
        model.addAttribute("exercises", exerciseService.findAllExercises());
        return "exercise/all";
    }

    @RequestMapping("/exercises/delete/{id}")
    public String delete(@PathVariable Long id) {
        exerciseService.deleteExerciseById(id);
        return "redirect:/exercise/all";
    }

    @GetMapping("/exercises/add")
    public String add(Model model) {
        model.addAttribute("exercise", new Exercise());
        return "exercise/add";
    }

    @PostMapping("/exercises/add")
    public String addProcess(@Valid Exercise exercise, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "exercise/add";
        }
        exerciseService.addExercise(exercise);
        return "redirect:/exercise/all";
    }

    @GetMapping("/exercises/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
            model.addAttribute("exercise", exerciseService.getSingleExerciseById(id));
            return "exercise/edit";
    }

    @PostMapping("/exercises/edit/{id}")
    public String editProcess(@Valid Exercise exercise, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "exercise/edit";
        }
        exerciseService.addExercise(exercise);
        return "redirect:/exercise/all";
    }

    @GetMapping("/exercise/show/{id}")
    public String show(@PathVariable Long id, Model model) {
            model.addAttribute("exercise", exerciseService.getSingleExerciseById(id));
            return "exercise/show";
    }
}

