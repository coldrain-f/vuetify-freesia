package edu.coldrain.freesia.repository;

import edu.coldrain.freesia.entity.Word;
import edu.coldrain.freesia.repository.querydsl.WordRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long>, WordRepositoryQuerydsl {
}
