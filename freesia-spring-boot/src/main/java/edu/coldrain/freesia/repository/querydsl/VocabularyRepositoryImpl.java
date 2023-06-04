package edu.coldrain.freesia.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.coldrain.freesia.dto.VocabularyDTO;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class VocabularyRepositoryImpl implements VocabularyRepositoryQuerydsl {

    private final JPAQueryFactory query;

    public VocabularyRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Optional<VocabularyDTO.Response>> findAllByVocabularyResponses(Pageable pageable) {
        return null;
    }
}
