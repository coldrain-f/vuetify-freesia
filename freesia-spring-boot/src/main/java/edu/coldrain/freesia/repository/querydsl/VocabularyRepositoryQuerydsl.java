package edu.coldrain.freesia.repository.querydsl;

import edu.coldrain.freesia.dto.VocabularyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VocabularyRepositoryQuerydsl {

    Page<VocabularyDTO.Response> searchVocabularyResponsePage(Pageable pageable);

    VocabularyDTO.Response searchOneVocabularyResponse(Long vocabularyId);
}
