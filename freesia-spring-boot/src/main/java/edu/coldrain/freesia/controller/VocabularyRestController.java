package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.dto.VocabularyDTO;
import edu.coldrain.freesia.service.VocabularyService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public List<VocabularyDTO.Response> findAll() {
        return vocabularyService.findAllVocabularyResponse();
    }
}
