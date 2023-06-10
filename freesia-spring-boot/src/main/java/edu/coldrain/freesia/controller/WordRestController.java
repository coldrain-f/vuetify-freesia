package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.dto.WordDTO;
import edu.coldrain.freesia.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WordRestController {

    private final WordService wordService;

    @PostMapping("/units/{unitId}/words")
    public Long registerWord(@PathVariable Long unitId, @RequestBody WordDTO.RegistrationRequest request) {
        return wordService.registerWord(unitId, request);
    }

    @GetMapping("/units/{unitId}/words")
    public Page<WordDTO.Response> searchWordResponsePage(@PathVariable Long unitId, @PageableDefault(size = 3) Pageable pageable) {
        return wordService.searchWordResponsePage(unitId, pageable);
    }
}
