package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.dto.VocabularyDTO;
import edu.coldrain.freesia.dto.VocabularySearchCondition;
import edu.coldrain.freesia.service.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vocabulary")
@RequiredArgsConstructor
public class VocabularyRestController {

    private final VocabularyService vocabularyService;

    @PostMapping
    public Long register(@RequestBody VocabularyDTO.RegistrationRequest request) {
        return vocabularyService.register(request);
    }

    @PatchMapping("/{vocabularyId}")
    public void modifyById(@RequestBody VocabularyDTO.ModifyRequest request, @PathVariable Long vocabularyId) {
        vocabularyService.modifyById(request, vocabularyId);
    }

    @DeleteMapping("/{vocabularyId}")
    public void removeById(@PathVariable Long vocabularyId) {
        vocabularyService.removeById(vocabularyId);
    }

    @GetMapping
    public Page<VocabularyDTO.Response> searchResponsePage(
            @PageableDefault(size = 3) Pageable pageable,
            @ModelAttribute VocabularySearchCondition searchCondition) {
        return vocabularyService.searchResponsePage(pageable, searchCondition);
    }

    @GetMapping("/{vocabularyId}")
    public VocabularyDTO.Response getResponseById(@PathVariable Long vocabularyId) {
        return vocabularyService.getResponseById(vocabularyId);
    }
}
