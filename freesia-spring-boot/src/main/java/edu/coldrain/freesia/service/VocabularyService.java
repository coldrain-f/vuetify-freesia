package edu.coldrain.freesia.service;

import edu.coldrain.freesia.dto.VocabularyDTO;
import edu.coldrain.freesia.entity.Language;
import edu.coldrain.freesia.entity.Vocabulary;
import edu.coldrain.freesia.exception.LanguageNotFoundException;
import edu.coldrain.freesia.exception.VocabularyNotFoundException;
import edu.coldrain.freesia.repository.LanguageRepository;
import edu.coldrain.freesia.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional // 없으면 변경 감지 안 됨.
    public void modifyVocabulary(VocabularyDTO.ModifyRequest request, Long vocabularyId) {
        final Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyNotFoundException("Vocabulary not found exception."));

        vocabulary.changeTitle(request.getTitle());
    }

    public void removeVocabularyById(Long vocabularyId) {
        vocabularyRepository.deleteById(vocabularyId);
    }

    public List<VocabularyDTO.Response> findAllVocabularyResponse() {
        return vocabularyRepository.findAll()
                .stream()
                .map((v) -> VocabularyDTO.Response
                        .builder()
                        .id(v.getId())
                        .title(v.getTitle())
                        .language(v.getLanguage().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public Page<VocabularyDTO.Response> searchVocabularyResponsePage(Pageable pageable) {
        return vocabularyRepository.searchVocabularyResponsePage(pageable);
    }

    public VocabularyDTO.Response searchOneVocabularyResponse(Long vocabularyId) {
        return vocabularyRepository.searchOneVocabularyResponse(vocabularyId);
    }
}
