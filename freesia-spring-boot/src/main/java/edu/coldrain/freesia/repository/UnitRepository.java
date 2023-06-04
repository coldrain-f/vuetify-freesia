package edu.coldrain.freesia.repository;

import edu.coldrain.freesia.entity.Unit;
import edu.coldrain.freesia.repository.querydsl.UnitRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long>, UnitRepositoryQuerydsl {
}
