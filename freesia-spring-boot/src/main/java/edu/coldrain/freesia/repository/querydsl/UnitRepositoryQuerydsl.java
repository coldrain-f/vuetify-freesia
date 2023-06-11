package edu.coldrain.freesia.repository.querydsl;

import edu.coldrain.freesia.dto.UnitDTO;
import edu.coldrain.freesia.entity.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UnitRepositoryQuerydsl {

    Page<UnitDTO.Response> searchUnitResponsePage(Long vocabularyId, Pageable pageable);

    UnitDTO.Response searchOneUnitResponse(Long unitId);

    List<Unit> findAllByParentId(Long vocabularyId);
}
