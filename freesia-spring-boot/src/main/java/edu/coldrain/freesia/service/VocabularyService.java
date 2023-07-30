package edu.coldrain.freesia.service;

import edu.coldrain.freesia.dto.VocabularyDTO;
import edu.coldrain.freesia.dto.VocabularySearchCondition;
import edu.coldrain.freesia.entity.*;
import edu.coldrain.freesia.exception.LanguageNotFoundException;
import edu.coldrain.freesia.exception.VocabularyNotFoundException;
import edu.coldrain.freesia.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;

    private final LanguageRepository languageRepository;

    private final UnitRepository unitRepository;

    private final WordRepository wordRepository;

    private final PlannerRepository plannerRepository;

    private final PlannerDetailRepository plannerDetailRepository;

    public Long register(VocabularyDTO.RegistrationRequest request) {
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
    public void modifyById(VocabularyDTO.ModifyRequest request, Long vocabularyId) {
        final Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyNotFoundException("Vocabulary not found exception."));

        final Language language = languageRepository.findByName(request.getLanguage())
                        .orElseThrow(() -> new LanguageNotFoundException("Language not found exception."));

        vocabulary.modifyVocabulary(request.getTitle(), language);
    }

    @Transactional
    public void removeById(Long vocabularyId) {
        // Delete Planner And PlannerDetails
        final Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyNotFoundException("vocabulary not found exception."));

        final Planner planner = plannerRepository.findByName(vocabulary.getTitle() + " Planner")
                .orElseThrow(() -> new IllegalArgumentException("planner not found exception."));

        final List<PlannerDetail> plannerDetails = plannerDetailRepository.findAllByPlannerId(planner.getId());
        plannerDetailRepository.deleteAllInBatch(plannerDetails);
        plannerRepository.deleteById(planner.getId());

        // Delete Units
        final List<Unit> units = unitRepository.findAllByParentId(vocabularyId);
        if (units.isEmpty()) {
            // Delete vocabulary
            vocabularyRepository.deleteById(vocabularyId);
            return;
        }
        units.forEach((u) -> {
            final List<Word> words = wordRepository.findAllByParentId(u.getId());
            // Delete all words
            wordRepository.deleteAllInBatch(words);
        });
        // Delete all units
        unitRepository.deleteAllInBatch(units);

        // Delete vocabulary
        vocabularyRepository.deleteById(vocabularyId);
    }

    public Page<VocabularyDTO.Response> searchResponsePage(Pageable pageable, VocabularySearchCondition searchCondition) {
        return vocabularyRepository.searchResponsePage(pageable, searchCondition);
    }

    public VocabularyDTO.Response getResponseById(Long vocabularyId) {
        return vocabularyRepository.getResponseById(vocabularyId);
    }
}
