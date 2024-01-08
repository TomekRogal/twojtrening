package pl.coderslab.twojtrening.user;

public interface UserService {

    User findByUserName(String name);

    void saveUser(User user);
}