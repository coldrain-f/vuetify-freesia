package edu.coldrain.freesia.repository.querydsl;

import edu.coldrain.freesia.dto.WordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WordRepositoryQuerydsl {

    Page<WordDTO.Response> searchWordResponsePage(Long unitId, Pageable pageable);
}
