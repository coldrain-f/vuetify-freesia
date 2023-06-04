package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.dto.UnitDTO;
import edu.coldrain.freesia.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/units")
@RequiredArgsConstructor
public class UnitRestController {

    private final UnitService unitService;

    @GetMapping
    public Page<UnitDTO.Response> searchUnitResponsePage(@PageableDefault(size = 3) Pageable pageable) {
        return unitService.searchUnitResponsePage(pageable);
    }

    @GetMapping("/{unitId}")
    public UnitDTO.Response searchOneUnitResponse(@PathVariable Long unitId) {
        return unitService.searchOneUnitResponse(unitId);
    }
}
