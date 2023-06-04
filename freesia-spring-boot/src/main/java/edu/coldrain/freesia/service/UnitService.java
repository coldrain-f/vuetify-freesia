package edu.coldrain.freesia.service;

import edu.coldrain.freesia.dto.UnitDTO;
import edu.coldrain.freesia.entity.Unit;
import edu.coldrain.freesia.entity.Vocabulary;
import edu.coldrain.freesia.exception.UnitNotFoundException;
import edu.coldrain.freesia.exception.VocabularyNotFoundException;
import edu.coldrain.freesia.repository.UnitRepository;
import edu.coldrain.freesia.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    private final VocabularyRepository vocabularyRepository;

    public Page<UnitDTO.Response> searchUnitResponsePage(@PageableDefault(size = 3) Pageable pageable) {
        return unitRepository.searchUnitResponsePage(pageable);
    }

    public UnitDTO.Response searchOneUnitResponse(Long unitId) {
        return unitRepository.searchOneUnitResponse(unitId);
    }

    public void removeUnit(Long unitId) {
        unitRepository.deleteById(unitId);
    }

    @Transactional
    public void modifyUnit(Long unitId, UnitDTO.ModifyRequest request) {
        final Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new UnitNotFoundException("Unit not found exception."));

        unit.changeSubject(request.getSubject());
    }

    public Long registerUnit(Long vocabularyId, UnitDTO.RegistrationRequest request) {
        final Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyNotFoundException("Vocabulary not found exception."));

        final Unit unit = Unit.builder()
                .vocabulary(vocabulary)
                .subject(request.getSubject())
                .build();

        final Unit savedUnit = unitRepository.save(unit);
        return savedUnit.getId();
    }
}
