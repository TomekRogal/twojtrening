package pl.coderslab.twojtrening.exercise;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ExerciseControllerTest {
    @Autowired
    private MockMvc mockMvc;



    @Test
    void shouldFindAllExercises() throws Exception {

//        given(exerciseService.findAllExercises()).willReturn(List.of(new Exercise(), new Exercise()));
        //when
        mockMvc.perform(get("/exercise/all"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("exercise/all"))
                .andExpect(model().attribute("exercises", hasSize(2)))
                .andReturn();
        //then
    }

    @Test
    void delete() {
    }

    @Test
    void add() {
    }

    @Test
    void addProcess() {
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