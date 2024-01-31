package pl.coderslab.twojtrening.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
                .param("confirm", password)
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
                .param("confirm", password)
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
                .param("confirm", password)
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
                .param("confirm", "wrongPassword")
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
    @WithUserDetails("test")
    void shouldNotDeleteUserWrongId() throws Exception {
        mockMvc.perform(get("/users/delete/10"))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldNotDeleteStillEnabledUser() throws Exception {
        mockMvc.perform(get("/users/delete/4"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/users/all"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowUserDetails() throws Exception {
        mockMvc.perform(get("/user/details"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/details"))
                .andReturn();
    }

    @Test
    @WithUserDetails
    void shouldShowEditUsernameForm() throws Exception {
        mockMvc.perform(get("/user/edit"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/edit"))
                .andExpect(model().attribute("user", notNullValue()))
                .andReturn();
    }

    @Test
    @WithUserDetails
    void shouldEditUsernameFormProcess() throws Exception {
        User user = userService.findByUserName("user");
        user.setUsername("editedUser");
        MockHttpServletRequestBuilder request = post("/user/edit")
                .flashAttr("user", user)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
        assertThat(user.getUsername()).isEqualTo(userService.findLoggedUser(user).getUsername());
        user.setUsername("user");
        userService.saveUser(user);
    }

    @Test
    @WithUserDetails
    void shouldNotEditUsernameFormProcessWrongUsername() throws Exception {
        User user = userService.findByUserName("user");
        user.setUsername("");
        MockHttpServletRequestBuilder request = post("/user/edit")
                .flashAttr("user", user)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/edit"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
        assertNotEquals(user.getUsername(), userService.findLoggedUser(user).getUsername());
    }

    @Test
    @WithUserDetails
    void shouldNotEditUsernameFormProcessUsernameTaken() throws Exception {
        User user = userService.findByUserName("user");
        user.setUsername("test");
        MockHttpServletRequestBuilder request = post("/user/edit")
                .flashAttr("user", user)
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(model().attribute("register", "failed"))
                .andExpect(view().name("user/edit"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
        assertNotEquals(user.getUsername(), userService.findLoggedUser(user).getUsername());
    }

    @Test
    @WithUserDetails
    void shouldShowEditPasswordForm() throws Exception {
        mockMvc.perform(get("/user/password"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/password"))
                .andExpect(model().attribute("user", notNullValue()))
                .andReturn();
    }

    @Test
    @WithUserDetails
    void shouldEditPasswordFormProcess() throws Exception {
        String password = "editedPassword";
        User user = userService.findByUserName("user");
        user.setPassword(password);
        MockHttpServletRequestBuilder request = post("/user/password")
                .flashAttr("user", user)
                .param("confirm", password)
                .param("old", passwordEncoder.encode("user"))
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
        assertThat(user.getPassword()).isEqualTo(userService.findLoggedUser(user).getPassword());
        user.setPassword("user");
        userService.saveUser(user);
    }

    @Test
    @WithUserDetails
    void shouldNotEditPasswordFormProcessWrongPassword() throws Exception {
        String password = "";
        User user = userService.findByUserName("user");
        user.setPassword(password);
        MockHttpServletRequestBuilder request = post("/user/password")
                .flashAttr("user", user)
                .param("confirm", password)
                .param("old", passwordEncoder.encode("user"))
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/password"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
        assertNotEquals(user.getPassword(), userService.findLoggedUser(user).getPassword());
    }

    @Test
    @WithUserDetails
    void shouldNotEditPasswordFormProcessWrongPasswordConfirm() throws Exception {
        String password = "editedPassword";
        User user = userService.findByUserName("user");
        user.setPassword(password);
        MockHttpServletRequestBuilder request = post("/user/password")
                .flashAttr("user", user)
                .param("confirm", "wrongPassword")
                .param("old", passwordEncoder.encode("user"))
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/password"))
                .andExpect(model().attribute("pass", "failed"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
        assertNotEquals(user.getPassword(), userService.findLoggedUser(user).getPassword());
    }

    @Test
    @WithUserDetails
    void shouldNotEditPasswordFormProcessNewPasswordSameAsOld() throws Exception {
        String password = "user";
        User user = userService.findByUserName("user");
        user.setPassword(password);
        MockHttpServletRequestBuilder request = post("/user/password")
                .flashAttr("user", user)
                .param("confirm", password)
                .param("old", passwordEncoder.encode(password))
                .with(csrf());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/password"))
                .andExpect(model().attribute("old", "failed"))
                .andReturn();
        assertThat(userService.findAllUsers().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("test")
    void shouldShowAllUsers() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/all"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andReturn();
    }

    @Test
    @WithUserDetails
    void shouldShowActivateUserForm() throws Exception {
        User user = userService.findByUserName("user");
        user.setEnabled(0);
        userService.saveUser(user);
        mockMvc.perform(get("/activate"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/activate"))
                .andReturn();
        assertThat(userService.findByUserName("user").getEnabled()).isEqualTo(user.getEnabled());
        user.setEnabled(1);
        userService.saveUser(user);
    }

    @Test
    @WithUserDetails
    void shouldNotShowActivateUserForm() throws Exception {
        User user = userService.findByUserName("user");
        mockMvc.perform(get("/activate"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/"))
                .andReturn();
        assertThat(userService.findByUserName("user").getEnabled()).isEqualTo(user.getEnabled());
    }

    @Test
    @WithUserDetails
    void shouldActivateUser() throws Exception {
        User user = userService.findByUserName("user");
        user.setEnabled(0);
        userService.saveUser(user);
        mockMvc.perform(post("/activate")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/"))
                .andReturn();
        assertThat(userService.findByUserName("user").getEnabled()).isEqualTo(1);
    }

    @Test
    @WithUserDetails
    void shouldShowInactivateUserForm() throws Exception {
        mockMvc.perform(get("/user/inactivate"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(view().name("user/inactivate"))
                .andReturn();
    }

    @Test
    @WithUserDetails
    void shouldInactivateUser() throws Exception {
        User user = userService.findByUserName("user");
        mockMvc.perform(post("/user/inactivate")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/"))
                .andReturn();
        assertThat(userService.findByUserName("user").getEnabled()).isEqualTo(0);
        user.setEnabled(1);
        userService.saveUser(user);
    }
}