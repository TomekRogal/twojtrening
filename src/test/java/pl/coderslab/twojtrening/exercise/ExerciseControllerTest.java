package pl.coderslab.twojtrening.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ExerciseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ExerciseService exerciseService;

    @Test
    void shouldFindAllExercises() throws Exception {
        mockMvc.perform(get("/exercise/all"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("exercise/all"))
                .andExpect(model().attribute("exercises", hasSize(2)))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldDeleteExercise() throws Exception {
        mockMvc.perform(get("/exercises/delete/3"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/exercise/all"))
                .andReturn();
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(1);
        exerciseService.addExercise(Exercise.builder().name("Ćwiczenie 1").description("Ćwiczenie 1").build());
    }

    @Test
    @WithUserDetails("test")
    void shouldNotDeleteExerciseWrongId() throws Exception {
        mockMvc.perform(get("/exercises/delete/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowAddExerciseForm() throws Exception {
        mockMvc.perform(get("/exercises/add"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("exercise/add"))
                .andExpect(model().attribute("exercise", notNullValue()))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldAddExerciseFormProcess() throws Exception {
        Exercise exercise = new Exercise();
        MockHttpServletRequestBuilder request = post("/exercises/add")
                .flashAttr("exercise", exercise)
                .param("id", "")
                .param("name", "test")
                .param("description", "test")
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/exercise/all"))
                .andReturn();
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(3);
        exerciseRepository.delete(exercise);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotAddExerciseFormProcess() throws Exception {
        Exercise exercise = new Exercise();
        MockHttpServletRequestBuilder request = post("/exercises/add")
                .flashAttr("exercise", exercise)
                .param("id", "")
                .param("name", "")
                .param("description", "test")
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("exercise/add"))
                .andReturn();
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowEditExerciseForm() throws Exception {
        mockMvc.perform(get("/exercises/edit/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("exercise/edit"))
                .andExpect(model().attribute("exercise", notNullValue()))
                .andReturn();
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(2);
    }
    @Test
    @WithUserDetails("test")
    void shouldNotShowEditExerciseFormWrongId() throws Exception {
        mockMvc.perform(get("/exercises/edit/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldEditExerciseFormProcess() throws Exception {
        Exercise exercise = exerciseService.getSingleExerciseById(4L);
        exercise.setName("testEdit");
        exercise.setDescription("testEdit");
        MockHttpServletRequestBuilder requst = post("/exercises/edit/4")
                .flashAttr("exercise", exercise)
                .param("id", exercise.getId().toString())
                .param("name", exercise.getName())
                .param("description", exercise.getDescription())
                .with(csrf());
        mockMvc.perform(requst)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/exercise/all"))
                .andReturn();
        Exercise editedExercise = exerciseService.getSingleExerciseById(4L);
        assertThat(editedExercise.getName()).isEqualTo(exercise.getName());
        assertThat(editedExercise.getDescription()).isEqualTo(exercise.getDescription());
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotEditExerciseFormProcess() throws Exception {
        Exercise exercise = exerciseService.getSingleExerciseById(4L);
        exercise.setName("");
        exercise.setDescription("testEdit");
        MockHttpServletRequestBuilder request = post("/exercises/edit/4")
                .flashAttr("exercise", exercise)
                .param("id", exercise.getId().toString())
                .param("name", exercise.getName())
                .param("description", exercise.getDescription())
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("exercise/edit"))
                .andReturn();
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(2);
    }
    @Test
    @WithUserDetails("test")
    void shouldShowSingleExercise() throws Exception {
        mockMvc.perform(get("/exercise/show/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("exercise/show"))
                .andExpect(model().attribute("exercise", notNullValue()))
                .andReturn();
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(2);
    }
    @Test
    @WithUserDetails("test")
    void shouldNotShowSingleExerciseWrongId() throws Exception {
        mockMvc.perform(get("/exercise/show/10"))
                .andDo(print())
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(exerciseService.findAllExercises().size()).isEqualTo(2);
    }
}