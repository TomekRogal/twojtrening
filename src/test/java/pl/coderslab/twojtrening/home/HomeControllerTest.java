package pl.coderslab.twojtrening.home;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("test")
    void shouldShowHomePageWithUser() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("home/home"))
                .andExpect(model().attribute("loggedUser", notNullValue()))
                .andReturn();
    }

    @Test
    void shouldShowHomePageWithoutUser() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("home/home"))
                .andExpect(model().attribute("loggedUser", nullValue()))
                .andReturn();
    }
}