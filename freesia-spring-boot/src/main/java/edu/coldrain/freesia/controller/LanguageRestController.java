package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.dto.LanguageDTO;
import edu.coldrain.freesia.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/language")
public class LanguageRestController {

    private final LanguageService languageService;

    @GetMapping
    public List<LanguageDTO.Response> findAll() {
        return languageService.findAll();
    }
}
