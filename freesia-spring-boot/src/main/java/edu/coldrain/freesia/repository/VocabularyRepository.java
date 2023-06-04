package edu.coldrain.freesia.repository;

import edu.coldrain.freesia.entity.Vocabulary;
import edu.coldrain.freesia.repository.querydsl.VocabularyRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long>, VocabularyRepositoryQuerydsl {
}
