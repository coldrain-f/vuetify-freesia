package edu.coldrain.freesia.service;

import edu.coldrain.freesia.entity.Planner;
import edu.coldrain.freesia.entity.Vocabulary;
import edu.coldrain.freesia.exception.VocabularyNotFoundException;
import edu.coldrain.freesia.repository.PlannerRepository;
import edu.coldrain.freesia.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;

    private final VocabularyRepository vocabularyRepository;

    public void createPlanner(Long vocabularyId) {
        final Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyNotFoundException("vocabulary not found exception."));

    }
}
