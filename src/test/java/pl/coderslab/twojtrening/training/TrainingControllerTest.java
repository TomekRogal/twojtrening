package pl.coderslab.twojtrening.training;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
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

@SpringBootTest
@AutoConfigureMockMvc
class TrainingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private UserService userService;

    @Test
    @WithUserDetails("test")
    void shouldFindAllTrainingsFromUser() throws Exception {
        mockMvc.perform(get("/training/all"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("training/all"))
                .andExpect(model().attribute("trainings", hasSize(2)))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldDeletePlan() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/training/delete/3"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/training/all"))
                .andReturn();
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(1);
        trainingService.addTraining(Training.builder().name("Training 1").description("Training 1").user(user).build());
    }

    @Test
    @WithUserDetails("test")
    void shouldNotDeletePlan() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/training/delete/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowAddPlanForm() throws Exception {
        mockMvc.perform(get("/training/add"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("training/add"))
                .andExpect(model().attribute("training", notNullValue()))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldAddPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = Training.builder()
                .user(user).name("test").description("test")
                .build();
        MockHttpServletRequestBuilder request = post("/training/add")
                .flashAttr("training", training)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/training/all"))
                .andReturn();
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(3);
        trainingRepository.delete(training);
    }


    @Test
    @WithUserDetails("test")
    void shouldNotAddPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = Training.builder()
                .user(user).name("").description("test")
                .build();
        MockHttpServletRequestBuilder request = post("/training/add")
                .flashAttr("training", training)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("training/add"))
                .andReturn();
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowEditPlanForm() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/training/edit/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("training/edit"))
                .andExpect(model().attribute("training", notNullValue()))
                .andReturn();
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotShowEditPlanFormWrongId() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/training/edit/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(2);
    }


    @Test
    @WithUserDetails("test")
    void shouldEditPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        training.setName("testName");
        training.setDescription("testDescription");
        MockHttpServletRequestBuilder request = post("/training/edit/4")
                .flashAttr("training", training)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/training/all"))
                .andReturn();
        Training editedTraining = trainingService.getSingleTrainingById(4L, user);
        assertThat(editedTraining.getName()).isEqualTo(training.getName());
        assertThat(editedTraining.getDescription()).isEqualTo(training.getDescription());
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotEditPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Training training = trainingService.getSingleTrainingById(4L, user);
        training.setName("");
        MockHttpServletRequestBuilder request = post("/training/edit/4")
                .flashAttr("training", training)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("training/edit"))
                .andReturn();
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowSinglePlan() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/training/show/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("training/show"))
                .andExpect(model().attribute("training", notNullValue()))
                .andExpect(model().attribute("exercises", hasSize(2)))
                .andReturn();
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotShowSinglePlan() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/training/show/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(trainingService.findAllTrainingsFromUser(user).size()).isEqualTo(2);
    }
}