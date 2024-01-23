package pl.coderslab.twojtrening.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

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
        assertThat(exerciseRepository.findAll().size()).isEqualTo(1);
        exerciseRepository.save(Exercise.builder().id(3L).name("Ćwiczenie 1").description("Ćwiczenie 1").build());
    }

    @Test
    @WithUserDetails("test")
    void shouldNotDeleteExerciseWrongId() throws Exception {
        mockMvc.perform(get("/exercises/delete/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(exerciseRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldAddExerciseForm() throws Exception {
        mockMvc.perform(get("/exercises/add"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("exercise/add"))
                .andExpect(model().attribute("exercise", notNullValue()))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldAddExerciseFormPost() throws Exception {
        Exercise exercise = new Exercise();
        mockMvc.perform(post("/exercises/add")
                        .flashAttr("exercise", exercise)
                        .param("id", "")
                        .param("name", "test")
                        .param("description", "test")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/exercise/all"))
                .andReturn();
        assertThat(exerciseRepository.findAll().size()).isEqualTo(3);
        exerciseRepository.delete(exercise);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotAddExerciseFormPost() throws Exception {
        Exercise exercise = new Exercise();
        mockMvc.perform(post("/exercises/add")
                        .flashAttr("exercise", exercise)
                        .param("id", "")
                        .param("name", "")
                        .param("description", "test")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("exercise/add"))
                .andReturn();
        assertThat(exerciseRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void edit() {
    }

    @Test
    void editProcess() {
    }

    @Test
    void show() {
    }
}