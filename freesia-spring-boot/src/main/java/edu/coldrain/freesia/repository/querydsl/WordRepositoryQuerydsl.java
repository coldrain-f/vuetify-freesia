package edu.coldrain.freesia.repository.querydsl;

import edu.coldrain.freesia.dto.WordDTO;
import edu.coldrain.freesia.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WordRepositoryQuerydsl {

    Page<WordDTO.Response> searchWordResponsePage(Long unitId, Pageable pageable);

    WordDTO.Response searchOneWordResponse(Long wordId);

    List<Word> findAllByParentId(Long unitId);

    List<WordDTO.LearningWordResponse> findLearningWords(List<String> unitList);
}
