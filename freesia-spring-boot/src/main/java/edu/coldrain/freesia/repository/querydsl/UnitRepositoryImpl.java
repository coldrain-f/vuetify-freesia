package edu.coldrain.freesia.repository.querydsl;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.coldrain.freesia.dto.QUnitDTO_Response;
import edu.coldrain.freesia.dto.UnitDTO;
import edu.coldrain.freesia.entity.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static edu.coldrain.freesia.entity.QUnit.unit;
import static edu.coldrain.freesia.entity.QVocabulary.vocabulary;
import static edu.coldrain.freesia.entity.QWord.word;

public class UnitRepositoryImpl implements UnitRepositoryQuerydsl {

    private final JPAQueryFactory query;

    private final EntityManager em;

    public UnitRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
        this.em = em;
    }


    @Override
    public Page<UnitDTO.Response> searchUnitResponsePage(Long vocabularyId, Pageable pageable) {
        final List<UnitDTO.Response> content = query.select(new QUnitDTO_Response(
                        unit.id,
                        unit.subject,
                        Expressions.asNumber(0L) // wordCount
                ))
                .from(unit)
                .innerJoin(unit.vocabulary, vocabulary)
                .where(vocabulary.id.eq(vocabularyId))
                .orderBy(unit.id.desc())
                .offset(pageable.getOffset()) // page number
                .limit(pageable.getPageSize()) // size
                .fetch();

        content.forEach((u) -> {
            final Long wordCount = query.select(word.count())
                    .from(word)
                    .innerJoin(word.unit, unit)
                    .where(unit.id.eq(u.getId()))
                    .fetchOne();

            u.setWordCount(wordCount);
        });

        final Long total = query.select(unit.count())
                .from(unit)
                .innerJoin(unit.vocabulary, vocabulary)
                .where(vocabulary.id.eq(vocabularyId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }

    @Override
    public UnitDTO.Response searchOneUnitResponse(Long unitId) {
        return query.select(new QUnitDTO_Response(
                        unit.id,
                        unit.subject,
                        JPAExpressions.select(word.count()) // wordCount
                                .from(word)
                                .innerJoin(word.unit, unit)
                                .where(unit.id.eq(unitId))
                ))
                .from(unit)
                .innerJoin(unit.vocabulary, vocabulary)
                .where(unit.id.eq(unitId))
                .fetchOne();
    }

    @Override
    public List<Unit> findAllByParentId(Long vocabularyId) {
        return query.selectFrom(unit)
                .innerJoin(unit.vocabulary, vocabulary)
                .where(vocabulary.id.eq(vocabularyId))
                .fetch();
    }

    @Override
    public Long findRownumById(Long unitId) {
        final Long vocabularyId = query.select(unit.vocabulary.id)
                .from(unit)
                .where(unit.id.eq(unitId))
                .fetchOne();

        final String SQL = "SELECT V.RN " +
                "FROM UNIT U " +
                "JOIN (" +
                "  SELECT UNIT_ID, ROWNUM AS RN " +
                "  FROM (" +
                "    SELECT UNIT_ID " +
                "    FROM UNIT " +
                "    WHERE VOCABULARY_ID = :vocabularyId " +
                "    ORDER BY UNIT_ID ASC" +
                "  )" +
                ") V ON U.UNIT_ID = V.UNIT_ID " +
                "WHERE U.UNIT_ID = :unitId";

        final Object result = em.createNativeQuery(SQL)
                .setParameter("vocabularyId", vocabularyId)
                .setParameter("unitId", unitId)
                .getSingleResult();

        return Long.parseLong(String.valueOf(result));
    }
}
