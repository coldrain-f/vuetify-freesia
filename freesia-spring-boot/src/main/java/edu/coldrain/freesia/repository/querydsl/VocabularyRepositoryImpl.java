package edu.coldrain.freesia.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.coldrain.freesia.dto.QVocabularyDTO_Response;
import edu.coldrain.freesia.dto.VocabularyDTO;
import edu.coldrain.freesia.dto.VocabularySearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static edu.coldrain.freesia.entity.QLanguage.language;
import static edu.coldrain.freesia.entity.QUnit.unit;
import static edu.coldrain.freesia.entity.QVocabulary.vocabulary;

public class VocabularyRepositoryImpl implements VocabularyRepositoryQuerydsl {

    private final JPAQueryFactory query;

    public VocabularyRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<VocabularyDTO.Response> searchResponsePage(Pageable pageable, VocabularySearchCondition searchCondition) {
        final List<VocabularyDTO.Response> content = query.select(
                        new QVocabularyDTO_Response(
                                vocabulary.id,
                                vocabulary.title,
                                language.name,
                                Expressions.asNumber(0L), // unitCount
                                vocabulary.createdAt,
                                vocabulary.modifiedAt
                        )
                )
                .from(vocabulary)
                .where(languageEq(searchCondition.getLanguage()))
                .innerJoin(vocabulary.language, language)
                .orderBy(vocabulary.id.desc())
                .offset(pageable.getOffset()) // page number
                .limit(pageable.getPageSize()) // size
                .fetch();

        content.forEach((v) -> {
            final Long unitCount = query.select(unit.count())
                    .from(unit)
                    .innerJoin(unit.vocabulary, vocabulary)
                    .where(vocabulary.id.eq(v.getId()))
                    .fetchOne();

            v.setUnitCount(unitCount);
        });

        final Long total = query.select(vocabulary.count())
                .from(vocabulary)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }

    private BooleanExpression languageEq(String language) {
        return StringUtils.hasText(language) ? vocabulary.language.name.eq(language) : null;
    }

    @Override
    public VocabularyDTO.Response getResponseById(Long vocabularyId) {
        return query.select(
                        new QVocabularyDTO_Response(
                                vocabulary.id,
                                vocabulary.title,
                                language.name,
                                JPAExpressions.select(unit.count()) // unitCount
                                        .from(unit)
                                        .innerJoin(unit.vocabulary, vocabulary)
                                        .where(unit.vocabulary.id.eq(vocabularyId)),
                                vocabulary.createdAt,
                                vocabulary.modifiedAt
                        )
                )
                .from(vocabulary)
                .innerJoin(vocabulary.language, language)
                .where(vocabulary.id.eq(vocabularyId))
                .fetchOne();
    }
}
