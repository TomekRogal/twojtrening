package pl.coderslab.twojtrening.trainingexercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.coderslab.twojtrening.exercise.Exercise;
import pl.coderslab.twojtrening.exercise.ExerciseService;
import pl.coderslab.twojtrening.training.Training;
import pl.coderslab.twojtrening.training.TrainingService;
import pl.coderslab.twojtrening.user.User;
import pl.coderslab.twojtrening.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@SpringBootTest
@AutoConfigureMockMvc
class TrainingExerciseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private TrainingExerciseRepository trainingExerciseRepository;
    @Autowired
    private TrainingExerciseService trainingExerciseService;
    @Test
    @WithUserDetails("test")
    void shouldShowAddExerciseToTrainingForm() throws Exception {
        mockMvc.perform(get("/training/exercise/add/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("trainingexercise/add"))
                .andExpect(model().attribute("exercises", hasSize(2)))
                .andExpect(model().attribute("trainingExercise", notNullValue()))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldAddExerciseToTrainingTFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        Exercise exercise = exerciseService.getSingleExerciseById(4L);
        TrainingExercise trainingExercise = TrainingExercise.builder()
                .training(training).exercise(exercise).weight(10.0).sets(10).reps(10)
                .build();
        MockHttpServletRequestBuilder request = post("/training/exercise/add")
                .flashAttr("trainingExercise", trainingExercise)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/training/show/4"))
                .andReturn();
        assertThat(trainingService.findAllExerciseFromTraining(training).size()).isEqualTo(3);
        trainingExerciseRepository.delete(trainingExercise);
    }
    @Test
    @WithUserDetails("test")
    void shouldNotAddExerciseToTrainingTFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        Exercise exercise = exerciseService.getSingleExerciseById(4L);
        TrainingExercise trainingExercise = TrainingExercise.builder()
                .training(training).exercise(exercise).weight(-5.0).sets(10).reps(10)
                .build();
        MockHttpServletRequestBuilder request = post("/training/exercise/add")
                .flashAttr("trainingExercise", trainingExercise)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("trainingexercise/add"))
                .andReturn();
        assertThat(trainingService.findAllExerciseFromTraining(training).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldDeleteTrainingExercise() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        Exercise exercise = exerciseService.getSingleExerciseById(4L);
        mockMvc.perform(get("/training/exercise/delete/3"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/training/show/4"))
                .andReturn();
        assertThat(trainingService.findAllExerciseFromTraining(training).size()).isEqualTo(1);
        trainingExerciseService.addExerciseToTraining(TrainingExercise.builder()
                        .training(training).exercise(exercise).weight(10.0).sets(10).reps(10).build());
    }

    @Test
    @WithUserDetails("test")
    void shouldNotDeleteTrainingExerciseWrongId() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        mockMvc.perform(get("/training/exercise/delete/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(trainingService.findAllExerciseFromTraining(training).size()).isEqualTo(2);
    }
    @Test
    @WithUserDetails("test")
    void shouldShowEditExerciseToTrainingForm() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        mockMvc.perform(get("/training/exercise/edit/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("trainingexercise/edit"))
                .andExpect(model().attribute("exercises", hasSize(2)))
                .andExpect(model().attribute("trainingExercise", notNullValue()))
                .andReturn();
        assertThat(trainingService.findAllExerciseFromTraining(training).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldEditTrainingExerciseFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        TrainingExercise trainingExercise = trainingExerciseService.getSingleTrainingWithExercisesById(4L,user);
        trainingExercise.setReps(1);
        trainingExercise.setWeight(1.0);
        MockHttpServletRequestBuilder request = post("/training/exercise/edit")
                .flashAttr("trainingExercise", trainingExercise)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/training/show/4"))
                .andReturn();
        TrainingExercise editedTrainingExercise = trainingExerciseService.getSingleTrainingWithExercisesById(4L,user);
        assertThat(editedTrainingExercise.getReps()).isEqualTo(trainingExercise.getReps());
        assertThat(editedTrainingExercise.getWeight()).isEqualTo(trainingExercise.getWeight());
        assertThat(trainingService.findAllExerciseFromTraining(training).size()).isEqualTo(2);
    }
    @Test
    @WithUserDetails("test")
    void shouldNotEditTrainingExerciseFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        TrainingExercise trainingExercise = trainingExerciseService.getSingleTrainingWithExercisesById(4L,user);
        trainingExercise.setReps(-10);
        MockHttpServletRequestBuilder request = post("/training/exercise/edit")
                .flashAttr("trainingExercise", trainingExercise)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("trainingexercise/edit"))
                .andReturn();
        assertThat(trainingService.findAllExerciseFromTraining(training).size()).isEqualTo(2);
    }
    @Test
    @WithUserDetails("test")
    void shouldShowAddSelectedExerciseToTrainingForm() throws Exception {
        mockMvc.perform(get("/training/exercise/addEx/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("trainingexercise/addex"))
                .andExpect(model().attribute("trainings", hasSize(2)))
                .andExpect(model().attribute("exercises", hasSize(2)))
                .andExpect(model().attribute("trainingExercise", notNullValue()))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldAddSelectedExerciseToTrainingTFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        Exercise exercise = exerciseService.getSingleExerciseById(4L);
        TrainingExercise trainingExercise = TrainingExercise.builder()
                .training(training).exercise(exercise).weight(10.0).sets(10).reps(10)
                .build();
        MockHttpServletRequestBuilder request = post("/training/exercise/addEx")
                .flashAttr("trainingExercise", trainingExercise)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/exercise/all"))
                .andReturn();
        assertThat(trainingService.findAllExerciseFromTraining(training).size()).isEqualTo(3);
        trainingExerciseRepository.delete(trainingExercise);
    }
    @Test
    @WithUserDetails("test")
    void shouldNotAddSelectedExerciseToTrainingTFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        Exercise exercise = exerciseService.getSingleExerciseById(4L);
        TrainingExercise trainingExercise = TrainingExercise.builder()
                .training(training).exercise(exercise).weight(10.0).sets(-1).reps(10)
                .build();
        MockHttpServletRequestBuilder request = post("/training/exercise/addEx")
                .flashAttr("trainingExercise", trainingExercise)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("trainingexercise/addex"))
                .andReturn();
        assertThat(trainingService.findAllExerciseFromTraining(training).size()).isEqualTo(2);
    }
}