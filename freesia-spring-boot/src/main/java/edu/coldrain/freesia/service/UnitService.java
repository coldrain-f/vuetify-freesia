package edu.coldrain.freesia.service;

import edu.coldrain.freesia.dto.UnitDTO;
import edu.coldrain.freesia.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    public Page<UnitDTO.Response> searchUnitResponsePage(@PageableDefault(size = 3) Pageable pageable) {
        return unitRepository.searchUnitResponsePage(pageable);
    }

    public UnitDTO.Response searchOneUnitResponse(Long unitId) {
        return unitRepository.searchOneUnitResponse(unitId);
    }
}
