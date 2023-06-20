package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.dto.WordDTO;
import edu.coldrain.freesia.repository.WordRepository;
import edu.coldrain.freesia.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordRestController {

    private final WordService wordService;

    private final WordRepository wordRepository;

    @PostMapping("/units/{unitId}/words")
    public Long registerWord(@PathVariable Long unitId, @RequestBody WordDTO.RegistrationRequest request) {
        return wordService.registerWord(unitId, request);
    }

    @GetMapping("/units/{unitId}/words")
    public Page<WordDTO.Response> searchWordResponsePage(@PathVariable Long unitId, @PageableDefault(size = 3) Pageable pageable) {
        return wordService.searchWordResponsePage(unitId, pageable);
    }

    @GetMapping("/words/{wordId}")
    public WordDTO.Response searchOneWordResponse(@PathVariable Long wordId) {
        return wordService.searchOneWordResponse(wordId);
    }

    @DeleteMapping("/words/{wordId}")
    public void removeWord(@PathVariable Long wordId) {
        wordService.removeWord(wordId);
    }

    @PatchMapping("/words/{wordId}")
    public void modifyWord(@PathVariable Long wordId, @RequestBody WordDTO.ModifyRequest request) {
        wordService.modifyWord(wordId, request);
    }

    @GetMapping("/vocabulary/{vocabularyId}/units/{unitId}/learning-words")
    public List<WordDTO.LearningWord> findLearningWords(@PathVariable Long vocabularyId, @PathVariable Long unitId) {
        return wordRepository.findAllLearningWordList(vocabularyId, unitId);
    }
}
