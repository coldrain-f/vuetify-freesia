package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.entity.Language;
import edu.coldrain.freesia.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final LanguageRepository languageRepository;

    @GetMapping("/home")
    public String home() {
        return "Hello World!";
    }

    @GetMapping("/languages")
    public List<Language> languages() {
        final Language englishLanguage = Language.builder()
                .name("English")
                .build();

        languageRepository.save(englishLanguage);
        return languageRepository.findAll();
    }
}
