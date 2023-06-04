package edu.coldrain.freesia.repository.querydsl;

import edu.coldrain.freesia.dto.UnitDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UnitRepositoryQuerydsl {

    Page<UnitDTO.Response> searchUnitResponsePage(Pageable pageable);

    UnitDTO.Response searchOneUnitResponse(Long unitId);
}
