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
        Plan plan = planService.getSinglePlanById(4L, user);
        Training training = trainingService.getSingleTrainingById(4L, user);
        DayName dayName = dayNameRepository.findById(1L).get();
        PlanTraining planTraining = PlanTraining.builder()
                .plan(plan).week(1).training(training).dayName(dayName)
                .build();
        MockHttpServletRequestBuilder request = post("/plan/training/add")
                .flashAttr("planTraining", planTraining)
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
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plantraining/add"))
                .andReturn();
        assertThat(planService.getSinglePlanWithTrainingsAndExercisesById(plan.getId(),user).size()).isEqualTo(2);

    }


    @Test
    @WithUserDetails("test")
    void shouldDeletePlanTraining() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = planService.getSinglePlanById(4L, user);
        Training training = trainingService.getSingleTrainingById(4L, user);
        DayName dayName = dayNameRepository.findById(3L).get();
        mockMvc.perform(get("/plan/training/delete/3"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/plan/show/4"))
                .andReturn();
        assertThat(planService.getSinglePlanWithTrainingsAndExercisesById(plan.getId(),user).size()).isEqualTo(1);
        planTrainingService.addTrainingToPlan(PlanTraining.builder()
                .plan(plan).training(training).dayName(dayName).week(3).build());
    }
    @Test
    @WithUserDetails("test")
    void shouldNotDeletePlanTrainingWrongId() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = planService.getSinglePlanById(4L, user);
        mockMvc.perform(get("/plan/training/delete/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(planService.getSinglePlanWithTrainingsAndExercisesById(plan.getId(),user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowEditTrainingToPlanForm() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = planService.getSinglePlanById(4L, user);
        mockMvc.perform(get("/plan/training/edit/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plantraining/edit"))
                .andExpect(model().attribute("trainings", hasSize(2)))
                .andExpect(model().attribute("planTraining", notNullValue()))
                .andExpect(model().attribute("days", hasSize(7)))
                .andReturn();
        assertThat(planService.getSinglePlanWithTrainingsAndExercisesById(plan.getId(),user).size()).isEqualTo(2);
    }
    @Test
    @WithUserDetails("test")
    void shouldEditPlanTrainingFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = planService.getSinglePlanById(4L, user);
        PlanTraining planTraining = planTrainingService.getSinglePlanWithTrainingsById(4L,user);
        DayName dayName = dayNameRepository.findById(7L).get();
        planTraining.setDayName(dayName);
        planTraining.setWeek(8);
        MockHttpServletRequestBuilder request = post("/plan/training/edit")
                .flashAttr("planTraining", planTraining)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/plan/show/4"))
                .andReturn();
        PlanTraining editetPlanTraining = planTrainingService.getSinglePlanWithTrainingsById(4L,user);
        assertThat(editetPlanTraining.getDayName().getId()).isEqualTo(planTraining.getDayName().getId());
        assertThat(editetPlanTraining.getWeek()).isEqualTo(planTraining.getWeek());
        assertThat(planService.getSinglePlanWithTrainingsAndExercisesById(plan.getId(),user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotEditPlanTrainingFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = planService.getSinglePlanById(4L, user);
        PlanTraining planTraining = planTrainingService.getSinglePlanWithTrainingsById(4L,user);
        planTraining.setWeek(-1);
        MockHttpServletRequestBuilder request = post("/plan/training/edit")
                .flashAttr("planTraining", planTraining)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plantraining/edit"))
                .andReturn();
        assertThat(planService.getSinglePlanWithTrainingsAndExercisesById(plan.getId(),user).size()).isEqualTo(2);
    }
}