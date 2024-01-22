package pl.coderslab.twojtrening.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }
    @Test
    void shouldFindUserByUsername() {
        String name = "testName";
        User user = User.builder()
                .username(name).password("test").enabled(1)
                .build();
        underTest.save(user);
        //when
        User foundedUser = underTest.findByUsername(name);
        //then
        assertThat(foundedUser).isEqualTo(user);

    }
}