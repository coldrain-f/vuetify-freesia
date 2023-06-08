package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.dto.WordDTO;
import edu.coldrain.freesia.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WordRestController {

    private final WordService wordService;

    @PostMapping("/units/{unitId}/words")
    public Long registerWord(@PathVariable Long unitId, WordDTO.RegistrationRequest request) {
        return wordService.registerWord(unitId, request);
    }
}
