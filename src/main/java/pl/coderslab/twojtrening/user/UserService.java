package pl.coderslab.twojtrening.user;

import java.util.List;

public interface UserService {

    User findByUserName(String name);

    void saveNewUser(User user);
    void deleteUser(Long id);
    User findLoggedUser(User user);
    void saveUser(User user);
    List<User> findAllUsers();
}