package edu.coldrain.freesia.controller;

import edu.coldrain.freesia.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlannerRestController {

    private final PlannerService plannerService;

    @PostMapping("/vocabulary/{vocabularyId}/planner")
    public void createPlanner(@PathVariable Long vocabularyId) {
        plannerService.createPlanner(vocabularyId);
    }
}
