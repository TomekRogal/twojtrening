package pl.coderslab.twojtrening.role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RoleRepositoryTest {
@Autowired
RoleRepository underTest;
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }
    @Test
    void shouldFindRoleByName() {
        //given
        String name = "test";
        Role role = new Role();
        role.setName(name);
        underTest.save(role);
        //when
        Role foundedRole = underTest.findByName(name);
        //then
        assertThat(foundedRole).isEqualTo(role);
    }
}