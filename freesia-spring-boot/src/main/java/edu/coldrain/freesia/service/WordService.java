package edu.coldrain.freesia.service;

import edu.coldrain.freesia.dto.WordDTO;
import edu.coldrain.freesia.entity.Unit;
import edu.coldrain.freesia.entity.Word;
import edu.coldrain.freesia.exception.UnitNotFoundException;
import edu.coldrain.freesia.exception.WordNotFoundException;
import edu.coldrain.freesia.repository.UnitRepository;
import edu.coldrain.freesia.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
                .furigana(StringUtils.hasText(request.getFurigana()) ? request.getFurigana() : null)
                .partOfSpeech(request.getPartOfSpeech())
                .unit(unit)
                .build();

        final Word savedWord = wordRepository.save(word);
        return savedWord.getId();
    }

    public Page<WordDTO.Response> searchWordResponsePage(Long unitId, Pageable pageable) {
        return wordRepository.searchWordResponsePage(unitId, pageable);
    }

    public void removeWord(Long wordId) {
        wordRepository.deleteById(wordId);
    }

    @Transactional
    public void modifyWord(Long wordId, WordDTO.ModifyRequest request) {
        final Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new WordNotFoundException("word not found exception"));

        word.changeStudyWord(request.getStudyWord());
        word.changeNativeWord(request.getNativeWord());
        word.changePartOfSpeech(request.getPartOfSpeech());
        word.changeFurigana(request.getFurigana());
    }

    public WordDTO.Response searchOneWordResponse(Long wordId) {
        return wordRepository.searchOneWordResponse(wordId);
    }
}
