package edu.coldrain.freesia.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.coldrain.freesia.dto.QVocabularyDTO_Response;
import edu.coldrain.freesia.dto.VocabularyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static edu.coldrain.freesia.entity.QLanguage.language;
import static edu.coldrain.freesia.entity.QVocabulary.vocabulary;

public class VocabularyRepositoryImpl implements VocabularyRepositoryQuerydsl {

    private final JPAQueryFactory query;

    public VocabularyRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<VocabularyDTO.Response> searchVocabularyResponsePage(Pageable pageable) {
        final List<VocabularyDTO.Response> content = query.select(
                        new QVocabularyDTO_Response(
                                vocabulary.id,
                                vocabulary.title,
                                language.name
                        )
                )
                .from(vocabulary)
                .innerJoin(vocabulary.language, language)
                .orderBy(vocabulary.id.desc())
                .offset(pageable.getOffset()) // page number
                .limit(pageable.getPageSize()) // size
                .fetch();

        final Long total = query.select(vocabulary.count())
                .from(vocabulary)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }
}
