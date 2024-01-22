package pl.coderslab.twojtrening.plantraining;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.twojtrening.plan.Plan;

import java.util.List;


public interface PlanTrainingRepository extends JpaRepository<PlanTraining, Long> {
    @Query("SELECT DISTINCT pt FROM PlanTraining pt JOIN FETCH pt.training JOIN FETCH pt.dayName WHERE pt.plan = ?1 ORDER BY pt.week, pt.dayName.displayOrder")
    List<PlanTraining> findAllTrainingsFromPlan(Plan plan);

    @Modifying
    @Transactional
    @Query("delete PlanTraining pt where pt.plan = ?1")
    void deleteAllFromPlan(Plan plan);
}
