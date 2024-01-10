package pl.coderslab.twojtrening.training;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.twojtrening.user.User;

import java.util.List;


public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByUser(User user);

    @Modifying
    @Transactional
    @Query("delete Training t where t.user = ?1")
    void deleteAllTrainingsFromUser(User user);
}
