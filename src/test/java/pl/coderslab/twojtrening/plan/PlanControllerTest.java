package pl.coderslab.twojtrening.plan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import pl.coderslab.twojtrening.security.SpringDataUserDetailsService;
import pl.coderslab.twojtrening.user.User;
import pl.coderslab.twojtrening.user.UserService;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Test
    @WithUserDetails("test")
    void findAll() throws Exception {
        //given
        mockMvc.perform(get("/plan/all"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("plan/all"))
                .andExpect(model().attribute("plans", hasSize(0)))
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