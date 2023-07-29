package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.entity.PlannerDetail;
import edu.coldrain.freesia.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlannerRestController {

    private final PlannerService plannerService;

    @PostMapping("/vocabulary/{vocabularyId}/planner")
    public void createPlanner(@PathVariable Long vocabularyId) {
        plannerService.createPlanner(vocabularyId);
    }

    @GetMapping("/vocabulary/{vocabularyId}/planner/planner-details")
    public List<PlannerDetail> findAllByVocabularyId(@PathVariable Long vocabularyId) {
        return plannerService.findAllByVocabularyId(vocabularyId);
    }

    @PostMapping("/vocabulary/{vocabularyId}/planner/checkDuplicate")
    public boolean checkDuplicate(@PathVariable Long vocabularyId) {
        return plannerService.checkDuplicate(vocabularyId);
    }
}
