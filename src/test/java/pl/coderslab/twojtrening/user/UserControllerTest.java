package pl.coderslab.twojtrening.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.coderslab.twojtrening.role.Role;
import pl.coderslab.twojtrening.role.RoleRepository;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/login"))
                .andReturn();
    }
    @Test
    void shouldShowRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/register"))
                .andExpect(model().attribute("user", notNullValue()))
                .andReturn();
    }

    @Test
    void shouldRegisterNewUser() throws Exception {
        String password = "testUser";
        User user = User.builder()
                .username("testUser").password(password)
                .build();
        MockHttpServletRequestBuilder request = post("/register")
                .flashAttr("user", user)
                .param("confirm",password)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/login"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(3);
        userRepository.delete(user);
    }

    @Test
    void shouldNotRegisterNewUserEmptyUsername() throws Exception {
        String password = "testUser";
        User user = User.builder()
                .username("").password(password)
                .build();
        MockHttpServletRequestBuilder request = post("/register")
                .flashAttr("user", user)
                .param("confirm",password)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/register"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
    }
    @Test
    void shouldNotRegisterNewUserUsernameTaken() throws Exception {
        String password = "testUser";
        User user = User.builder()
                .username("test").password(password)
                .build();
        MockHttpServletRequestBuilder request = post("/register")
                .flashAttr("user", user)
                .param("confirm",password)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/register"))
                .andExpect(model().attribute("register", "failed"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
    }
    @Test
    void shouldNotRegisterNewUserWrongPasswordConfirm() throws Exception {
        String password = "testUser";
        User user = User.builder()
                .username("testUser").password(password)
                .build();
        MockHttpServletRequestBuilder request = post("/register")
                .flashAttr("user", user)
                .param("confirm","wrongPassword")
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/register"))
                .andExpect(model().attribute("pass", "failed"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldDeleteUser() throws Exception {
        User user = userService.findByUserName("user");
        user.setEnabled(0);
        userService.saveUser(user);
        mockMvc.perform(get("/users/delete/4"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/users/all"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(1);
        userService.saveNewUser(User.builder()
                .username("user").password("user").build());
    }

    @Test
    void userDetails() {
    }

    @Test
    void edit() {
    }

    @Test
    void editProcess() {
    }

    @Test
    void editPassword() {
    }

    @Test
    void passwordProcess() {
    }

    @Test
    void allUsers() {
    }

    @Test
    void activate() {
    }

    @Test
    void activateProcess() {
    }

    @Test
    void inactivate() {
    }

    @Test
    void inactivateProcess() {
    }
}