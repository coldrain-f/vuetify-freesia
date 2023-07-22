package edu.coldrain.freesia.repository.querydsl;

import edu.coldrain.freesia.dto.VocabularyDTO;
import edu.coldrain.freesia.dto.VocabularySearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VocabularyRepositoryQuerydsl {

    Page<VocabularyDTO.Response> searchResponsePage(Pageable pageable, VocabularySearchCondition searchCondition);

    VocabularyDTO.Response getResponseById(Long vocabularyId);
}
