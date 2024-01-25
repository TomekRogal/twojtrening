package pl.coderslab.twojtrening.plantraining;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.coderslab.twojtrening.dayname.DayName;
import pl.coderslab.twojtrening.dayname.DayNameRepository;
import pl.coderslab.twojtrening.plan.Plan;
import pl.coderslab.twojtrening.plan.PlanService;
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

@SpringBootTest
@AutoConfigureMockMvc
class PlanTrainingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PlanTrainingService planTrainingService;
    @Autowired
    private UserService userService;
    @Autowired
    private PlanService planService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private DayNameRepository dayNameRepository;
    @Autowired
    private PlanTrainingRepository planTrainingRepository;

    @Test
    @WithUserDetails("test")
    void shouldShowAddTrainingToPlanForm() throws Exception {
        mockMvc.perform(get("/plan/training/add/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plantraining/add"))
                .andExpect(model().attribute("trainings", hasSize(2)))
                .andExpect(model().attribute("planTraining", notNullValue()))
                .andExpect(model().attribute("days", hasSize(7)))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldAddTrainingToPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        PlanTraining planTraining = new PlanTraining();
        Plan plan = planService.getSinglePlanById(4L, user);
        Training training = trainingService.getSingleTrainingById(4L, user);
        DayName dayName = dayNameRepository.findById(1L).get();
        planTraining.setPlan(plan);
        planTraining.setTraining(training);
        planTraining.setDayName(dayName);
        MockHttpServletRequestBuilder request = post("/plan/training/add")
                .flashAttr("planTraining", planTraining)
                .param("id", "")
                .param("week", "1")
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/plan/show/4"))
                .andReturn();
        assertThat(planService.getSinglePlanWithTrainingsAndExercisesById(plan.getId(),user).size()).isEqualTo(3);
        planTrainingRepository.delete(planTraining);
    }
    @Test
    @WithUserDetails("test")
    void shouldNotAddTrainingToPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = planService.getSinglePlanById(4L, user);
        PlanTraining planTraining = new PlanTraining();
        MockHttpServletRequestBuilder request = post("/plan/training/add")
                .flashAttr("planTraining", planTraining)
                .param("id", "")
                .param("week", "1")
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plantraining/add"))
                .andReturn();
        assertThat(planService.getSinglePlanWithTrainingsAndExercisesById(plan.getId(),user).size()).isEqualTo(2);
    }


    @Test
    void delete() {
    }

    @Test
    void edit() {
    }

    @Test
    void editProcess() {
    }
}