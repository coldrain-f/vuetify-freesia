package edu.coldrain.freesia.service;

import edu.coldrain.freesia.dto.VocabularyDTO;
import edu.coldrain.freesia.entity.Language;
import edu.coldrain.freesia.entity.Vocabulary;
import edu.coldrain.freesia.exception.LanguageNotFoundException;
import edu.coldrain.freesia.repository.LanguageRepository;
import edu.coldrain.freesia.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;

    private final LanguageRepository languageRepository;

    public Long registerVocabulary(VocabularyDTO.RegistrationRequest request) {
        final Language language = languageRepository.findByName(request.getLanguage())
                .orElseThrow(() -> new LanguageNotFoundException("Language not found exception."));

        final Vocabulary vocabulary = Vocabulary.builder()
                .title(request.getTitle())
                .language(language)
                .build();

        final Vocabulary savedVocabulary = vocabularyRepository.save(vocabulary);
        return savedVocabulary.getId();
    }

    public List<VocabularyDTO.Response> findAllVocabularyResponse() {
        return vocabularyRepository.findAll()
                .stream()
                .map((v) -> VocabularyDTO.Response
                        .builder()
                        .title(v.getTitle())
                        .language(v.getLanguage().getName())
                        .build())
                .collect(Collectors.toList());
    }
}
