package edu.coldrain.freesia.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.coldrain.freesia.dto.QWordDTO_Response;
import edu.coldrain.freesia.dto.WordDTO;
import edu.coldrain.freesia.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static edu.coldrain.freesia.entity.QUnit.unit;
import static edu.coldrain.freesia.entity.QWord.word;

public class WordRepositoryImpl implements WordRepositoryQuerydsl {

    private final JPAQueryFactory query;

    private final EntityManager em;

    public WordRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
        this.em = em;
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
                .innerJoin(word.unit, unit)
                .where(unit.id.eq(unitId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }

    @Override
    public List<Word> findAllByParentId(Long unitId) {
        return query.selectFrom(word)
                .innerJoin(word.unit, unit)
                .where(unit.id.eq(unitId))
                .fetch();
    }

    @Override
    public WordDTO.Response searchOneWordResponse(Long wordId) {
        return query.select(new QWordDTO_Response(
                        word.id
                        , word.studyWord
                        , word.nativeWord
                        , word.partOfSpeech
                ))
                .from(word)
                .where(word.id.eq(wordId))
                .fetchOne();
    }

    @Override
    public List<WordDTO.LearningWord> findAllLearningWordList() {
        final String sql = "SELECT W.WORD_ID, W.STUDY_WORD, W.NATIVE_WORD, W.PART_OF_SPEECH, N.RN FROM " +
                "(SELECT U.*, ROWNUM RN FROM UNIT U WHERE VOCABULARY_ID = :vocabularyId) N " +
                "INNER JOIN WORD W ON W.UNIT_ID = N.UNIT_ID " +
                "WHERE N.RN IN (:rn, :rn + 1) " +
                "ORDER BY N.RN ASC, W.WORD_ID DESC";

        List<Object[]> nativeResults = em.createNativeQuery(sql)
                .setParameter("vocabularyId", "8014")
                .setParameter("rn", 1)
                .getResultList();

        return nativeResults.stream()
                .map((item) ->
                        WordDTO.LearningWord.builder()
                                // String -> Integer, String -> Long
                                .id(Long.parseLong(String.valueOf(item[0])))
                                .studyWord((String) item[1])
                                .nativeWord((String) item[2])
                                .partOfSpeech((String) item[3])
                                .rn(Integer.parseInt(String.valueOf(item[4])))
                                .build())
                .collect(Collectors.toList());
    }
}