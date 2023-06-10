package edu.coldrain.freesia.service;

import edu.coldrain.freesia.dto.WordDTO;
import edu.coldrain.freesia.entity.Unit;
import edu.coldrain.freesia.entity.Word;
import edu.coldrain.freesia.exception.UnitNotFoundException;
import edu.coldrain.freesia.repository.UnitRepository;
import edu.coldrain.freesia.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    private final UnitRepository unitRepository;

    public Long registerWord(Long unitId, WordDTO.RegistrationRequest request) {
        final Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new UnitNotFoundException("Unit not found exception."));

        final Word word = Word.builder()
                .studyWord(request.getStudyWord())
                .nativeWord(request.getNativeWord())
                .partOfSpeech(request.getPartOfSpeech())
                .unit(unit)
                .build();

        final Word savedWord = wordRepository.save(word);
        return savedWord.getId();
    }

    public Page<WordDTO.Response> searchWordResponsePage(Long unitId, Pageable pageable) {
        return wordRepository.searchWordResponsePage(unitId, pageable);
    }
}
