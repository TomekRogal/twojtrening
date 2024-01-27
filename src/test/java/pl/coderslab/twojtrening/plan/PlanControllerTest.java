package pl.coderslab.twojtrening.plan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.coderslab.twojtrening.user.User;
import pl.coderslab.twojtrening.user.UserService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlanControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;
    @Autowired
    private PlanRepository planRepository;

    @Test
    @WithUserDetails("test")
    void shouldFindAllPlansFromUser() throws Exception {
        mockMvc.perform(get("/plan/all"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plan/all"))
                .andExpect(model().attribute("plans", hasSize(2)))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldDeletePlan() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/plan/delete/3"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/plan/all"))
                .andReturn();
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(1);
        planService.addPlan(Plan.builder().name("Plan 1").startDate(LocalDate.now()).weeks(4).user(user).build());
    }

    @Test
    @WithUserDetails("test")
    void shouldNotDeletePlan() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/plan/delete/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(2);
    }


    @Test
    @WithUserDetails("test")
    void shouldShowAddPlanForm() throws Exception {
        mockMvc.perform(get("/plan/add"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plan/add"))
                .andExpect(model().attribute("plan", notNullValue()))
                .andReturn();
    }

    @Test
    @WithUserDetails("test")
    void shouldAddPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = Plan.builder()
                .user(user).weeks(4).name("test")
                .build();
        MockHttpServletRequestBuilder request = post("/plan/add")
                .flashAttr("plan", plan)
                .param("startDate", "2024-01-09")
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/plan/all"))
                .andReturn();
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(3);
        planRepository.delete(plan);
    }


    @Test
    @WithUserDetails("test")
    void shouldNotAddPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = Plan.builder()
                .user(user).weeks(4).name("")
                .build();

        MockHttpServletRequestBuilder request = post("/plan/add")
                .flashAttr("plan", plan)
                .param("startDate", "2024-01-09")
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plan/add"))
                .andReturn();
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowEditPlanForm() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/plan/edit/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plan/edit"))
                .andExpect(model().attribute("plan", notNullValue()))
                .andReturn();
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotShowEditPlanFormWrongId() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/plan/edit/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldEditPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = planService.getSinglePlanById(4L, user);
        plan.setName("testName");
        plan.setWeeks(10);
        MockHttpServletRequestBuilder request = post("/plan/edit/4")
                .flashAttr("plan", plan)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/plan/all"))
                .andReturn();
        Plan editedPlan = planService.getSinglePlanById(4L, user);
        assertThat(editedPlan.getName()).isEqualTo(plan.getName());
        assertThat(editedPlan.getWeeks()).isEqualTo(plan.getWeeks());
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotEditPlanFormProcess() throws Exception {
        User user = userService.findByUserName("test");
        Plan plan = planService.getSinglePlanById(4L, user);
        plan.setName("");
        plan.setWeeks(10);
        MockHttpServletRequestBuilder request = post("/plan/edit/4")
                .flashAttr("plan", plan)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plan/edit"))
                .andReturn();
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowSinglePlan() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/plan/show/4"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plan/show"))
                .andExpect(model().attribute("plan", notNullValue()))
                .andExpect(model().attribute("trainingsList", aMapWithSize(2)))
                .andReturn();
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotShowSinglePlan() throws Exception {
        User user = userService.findByUserName("test");
        mockMvc.perform(get("/plan/show/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(planService.findAllPlansFromUser(user).size()).isEqualTo(2);
    }
}