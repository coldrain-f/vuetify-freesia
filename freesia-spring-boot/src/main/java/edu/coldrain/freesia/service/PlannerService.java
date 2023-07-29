package edu.coldrain.freesia.service;

import edu.coldrain.freesia.entity.Planner;
import edu.coldrain.freesia.entity.PlannerDetail;
import edu.coldrain.freesia.entity.Vocabulary;
import edu.coldrain.freesia.exception.VocabularyNotFoundException;
import edu.coldrain.freesia.repository.PlannerDetailRepository;
import edu.coldrain.freesia.repository.PlannerRepository;
import edu.coldrain.freesia.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;

    private final PlannerDetailRepository plannerDetailRepository;

    private final VocabularyRepository vocabularyRepository;


    @Transactional
    public void createPlanner(Long vocabularyId) {
        final Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyNotFoundException("vocabulary not found exception."));

        final Planner planner = Planner.builder()
                .name(vocabulary.getTitle() + " Planner")
                .vocabulary(vocabulary)
                .build();

        plannerRepository.save(planner);

        final List<PlannerDetail> plannerDetails = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            final PlannerDetail ongoingPlannerDetail = PlannerDetail.builder()
                    .studyDay("Day " + i)
                    .learningStatus("Ongoing")
                    .planner(planner)
                    .build();

            plannerDetails.add(ongoingPlannerDetail);
        }

        plannerDetailRepository.saveAll(plannerDetails);
    }

    public boolean checkDuplicate(Long vocabularyId) {
        final Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyNotFoundException("vocabulary not found exception."));

        return plannerRepository.findByName(vocabulary.getTitle() + " Planner").isPresent();
    }
}
