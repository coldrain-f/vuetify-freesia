package edu.coldrain.freesia.repository;

import edu.coldrain.freesia.entity.PlannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerDetailRepository extends JpaRepository<PlannerDetail, Long> {

    List<PlannerDetail> findAllByPlannerId(Long plannerId);
}
