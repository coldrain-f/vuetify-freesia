package edu.coldrain.freesia.repository;

import edu.coldrain.freesia.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LearnerRepository extends JpaRepository<Learner, Long> {

    public Optional<Learner> findByEmail(String email);
}
