package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.dto.VocabularyDTO;
import edu.coldrain.freesia.service.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vocabulary")
@RequiredArgsConstructor
public class VocabularyRestController {

    private final VocabularyService vocabularyService;

    @PostMapping
    public Long registerVocabulary(@RequestBody VocabularyDTO.RegistrationRequest request) {
        return vocabularyService.registerVocabulary(request);
    }

    @PatchMapping("/{vocabularyId}")
    public void modifyVocabulary(@RequestBody VocabularyDTO.ModifyRequest request, @PathVariable Long vocabularyId) {
        vocabularyService.modifyVocabulary(request, vocabularyId);
    }

    @DeleteMapping("/{vocabularyId}")
    public void removeVocabulary(@PathVariable Long vocabularyId) {
        vocabularyService.removeVocabularyById(vocabularyId);
    }

    @GetMapping
    public Page<VocabularyDTO.Response> searchVocabularyResponsePage(@PageableDefault(size = 3) Pageable pageable) {
        return vocabularyService.searchVocabularyResponsePage(pageable);
    }

    @GetMapping("/{vocabularyId}")
    public VocabularyDTO.Response searchOneVocabularyResponse(@PathVariable Long vocabularyId) {
        return vocabularyService.searchOneVocabularyResponse(vocabularyId);
    }
}
