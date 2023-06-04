package edu.coldrain.freesia.repository.querydsl;

import edu.coldrain.freesia.dto.VocabularyDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepositoryQuerydsl {

    List<Optional<VocabularyDTO.Response>> findAllByVocabularyResponses(Pageable pageable);
}
