package edu.coldrain.freesia.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.coldrain.freesia.dto.QWordDTO_Response;
import edu.coldrain.freesia.dto.WordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static edu.coldrain.freesia.entity.QUnit.unit;
import static edu.coldrain.freesia.entity.QWord.word;

public class WordRepositoryImpl implements WordRepositoryQuerydsl {

    private final JPAQueryFactory query;

    public WordRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<WordDTO.Response> searchWordResponsePage(Long unitId, Pageable pageable) {
        final List<WordDTO.Response> content = query.select(new QWordDTO_Response(
                        word.id,
                        word.studyWord,
                        word.nativeWord,
                        word.partOfSpeech
                ))
                .from(word)
                .innerJoin(word.unit, unit)
                .where(unit.id.eq(unitId))
                .orderBy(word.id.desc())
                .offset(pageable.getOffset()) // page number
                .limit(pageable.getPageSize()) // size
                .fetch();

        final Long total = query.select(word.count())
                .from(word)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }
}