package edu.coldrain.freesia.repository.querydsl;

import edu.coldrain.freesia.dto.VocabularyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VocabularyRepositoryQuerydsl {

    Page<VocabularyDTO.Response> searchResponsePage(Pageable pageable);

    VocabularyDTO.Response getResponseById(Long vocabularyId);
}
