package edu.coldrain.freesia.repository;

import edu.coldrain.freesia.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

    Optional<Planner> findByName(String name);
}
