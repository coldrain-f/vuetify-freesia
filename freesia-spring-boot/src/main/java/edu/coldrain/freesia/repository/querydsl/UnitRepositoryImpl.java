package edu.coldrain.freesia.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.coldrain.freesia.dto.QUnitDTO_Response;
import edu.coldrain.freesia.dto.UnitDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static edu.coldrain.freesia.entity.QUnit.unit;
import static edu.coldrain.freesia.entity.QVocabulary.vocabulary;

public class UnitRepositoryImpl implements UnitRepositoryQuerydsl {

    private final JPAQueryFactory query;

    public UnitRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<UnitDTO.Response> searchUnitResponsePage(Long vocabularyId, Pageable pageable) {
        final List<UnitDTO.Response> content = query.select(new QUnitDTO_Response(
                        unit.id,
                        unit.subject
                ))
                .from(unit)
                .innerJoin(unit.vocabulary, vocabulary)
                .where(vocabulary.id.eq(vocabularyId))
                .orderBy(unit.id.desc())
                .offset(pageable.getOffset()) // page number
                .limit(pageable.getPageSize()) // size
                .fetch();

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
                        unit.subject
                ))
                .from(unit)
                .innerJoin(unit.vocabulary, vocabulary)
                .where(unit.id.eq(unitId))
                .fetchOne();
    }
}
