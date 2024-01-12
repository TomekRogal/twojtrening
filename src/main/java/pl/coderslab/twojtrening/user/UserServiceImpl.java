package pl.coderslab.twojtrening.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.twojtrening.error.NotFoundException;
import pl.coderslab.twojtrening.plan.PlanRepository;
import pl.coderslab.twojtrening.role.Role;
import pl.coderslab.twojtrening.role.RoleRepository;
import pl.coderslab.twojtrening.training.TrainingRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PlanRepository planRepository;
    private final TrainingRepository trainingRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder, PlanRepository planRepository, TrainingRepository trainingRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.planRepository = planRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id:%s not found", id)));
        if(user.getEnabled()==0){
            planRepository.deleteAllPlansFromUser(user);
            trainingRepository.deleteAllTrainingsFromUser(user);
            userRepository.deleteById(user.getId());
        }
    }
    @Override
    public User findLoggedUser(User loggedUser) {
        return userRepository.findById(loggedUser.getId())
                .orElseThrow(() -> new NotFoundException(String.format("User with id:%s not found", loggedUser.getId())));
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}