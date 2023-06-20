package edu.coldrain.freesia.service;

import edu.coldrain.freesia.dto.UnitDTO;
import edu.coldrain.freesia.entity.Unit;
import edu.coldrain.freesia.entity.Vocabulary;
import edu.coldrain.freesia.entity.Word;
import edu.coldrain.freesia.exception.UnitNotFoundException;
import edu.coldrain.freesia.exception.VocabularyNotFoundException;
import edu.coldrain.freesia.repository.UnitRepository;
import edu.coldrain.freesia.repository.VocabularyRepository;
import edu.coldrain.freesia.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    private final VocabularyRepository vocabularyRepository;

    private final WordRepository wordRepository;

    public Page<UnitDTO.Response> searchUnitResponsePage(Long vocabularyId, Pageable pageable) {
        return unitRepository.searchUnitResponsePage(vocabularyId, pageable);
    }

    public UnitDTO.Response searchOneUnitResponse(Long unitId) {
        return unitRepository.searchOneUnitResponse(unitId);
    }

    @Transactional
    public void removeUnit(Long unitId) {
        // delete all children
        final List<Word> words = wordRepository.findAllByParentId(unitId);
        wordRepository.deleteAllInBatch(words);

        // delete parent
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

    public Long findRownumById(Long unitId) {
        return unitRepository.findRownumById(unitId);
    }
}
