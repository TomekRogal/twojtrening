package pl.coderslab.twojtrening.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.plan.PlanRepository;
import pl.coderslab.twojtrening.role.Role;
import pl.coderslab.twojtrening.role.RoleRepository;
import pl.coderslab.twojtrening.training.TrainingRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    PlanRepository planRepository;
    @Mock
    TrainingRepository trainingRepository;
    @InjectMocks
    UserServiceImpl underTest;
    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void shouldFindUserByUserName() {
        //given
        String name = "test";
        //when
        underTest.findByUserName(name);
        //then
        verify(userRepository).findByUsername(name);
    }

    @Test
    void shouldSaveNewUser() {
        //given
        User user = User.builder()
                .username("test").password("test")
                .build();
        Role role = new Role();
        role.setName("ROLE_USER");
        given(roleRepository.findByName(anyString())).willReturn(role);
        given(passwordEncoder.encode(anyString())).willReturn("test");
        //when
        underTest.saveNewUser(user);
        //then
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);

    }

    @Test
    void shouldDeleteUser() {
        //given
        long id = 1L;
        User user = User.builder()
                .id(id).enabled(0)
                .build();
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        //when
        underTest.deleteUser(id);
        //then
        verify(userRepository).findById(id);
        verify(planRepository).deleteAllPlansFromUser(user);
        verify(trainingRepository).deleteAllTrainingsFromUser(user);
        verify(userRepository).deleteById(id);
    }
    @Test
    void shouldNotDeleteUserWrongId() {
        //given
        long id = 1L;
        given(userRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteUser(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("User with id:%s not found", id));
        verify(planRepository,never()).deleteAllPlansFromUser(any());
        verify(trainingRepository,never()).deleteAllTrainingsFromUser(any());
        verify(userRepository,never()).deleteById(any());
    }
    @Test
    void shouldNotDeleteStillEnableUser() {
        //given
        long id = 1L;
        User user = User.builder()
                .id(id).enabled(1)
                .build();
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        //when
        underTest.deleteUser(id);
        //then
        verify(planRepository,never()).deleteAllPlansFromUser(any());
        verify(trainingRepository,never()).deleteAllTrainingsFromUser(any());
        verify(userRepository,never()).deleteById(any());
    }
    @Test
    void shouldFindLoggedUser() {
        //given
        long id = 1L;
        User user = User.builder()
                .id(id).build();
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        //when
        User loggedUser = underTest.findLoggedUser(user);
        //then
        verify(userRepository).findById(id);
        assertThat(loggedUser).isEqualTo(user);
    }
    @Test
    void shouldNotFindLoggedUser() {
        //given
        long id = 1L;
        User user = User.builder()
                .id(id).build();
        //when
        //then
        assertThatThrownBy(() -> underTest.findLoggedUser(user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("User with id:%s not found", id));
    }

    @Test
    void shouldSaveUser() {
        //given
        long id = 1L;
        User user = User.builder()
                .id(id).build();
        //when
        underTest.saveUser(user);
        //then
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);

    }

    @Test
    void findAllUsers() {
        //when
        underTest.findAllUsers();
        //then
        verify(userRepository).findAll();
    }
}