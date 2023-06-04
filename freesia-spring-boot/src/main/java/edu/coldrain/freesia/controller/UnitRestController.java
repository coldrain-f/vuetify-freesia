package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.dto.UnitDTO;
import edu.coldrain.freesia.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UnitRestController {

    private final UnitService unitService;

    @GetMapping("/units")
    public Page<UnitDTO.Response> searchUnitResponsePage(@PageableDefault(size = 3) Pageable pageable) {
        return unitService.searchUnitResponsePage(pageable);
    }

    @GetMapping("/units/{unitId}")
    public UnitDTO.Response searchOneUnitResponse(@PathVariable Long unitId) {
        return unitService.searchOneUnitResponse(unitId);
    }

    @DeleteMapping("/units/{unitId}")
    public void removeUnit(@PathVariable Long unitId) {
        unitService.removeUnit(unitId);
    }

    @PatchMapping("/units/{unitId}")
    public void modifyUnit(@PathVariable Long unitId, UnitDTO.ModifyRequest request) {
        unitService.modifyUnit(unitId, request);
    }

    @PostMapping("/vocabulary/{vocabularyId}/units")
    public Long registerUnit(@PathVariable Long vocabularyId, UnitDTO.RegistrationRequest request) {
        return unitService.registerUnit(vocabularyId, request);
    }
}
