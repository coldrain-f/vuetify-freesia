package edu.coldrain.freesia.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "PLANNER_DETAIL_SEQ_GENERATOR", sequenceName = "PLANNER_DETAIL_SEQ", allocationSize = 1)
public class PlannerDetail extends Timestamp {

    @Id
    @GeneratedValue
    @Column(name = "PLANNER_DETAIL_ID")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String studyDay;

    @Column(nullable = false)
    private String learningStatus; // Finished, Ongoing

    @Column
    private String today;

    @Column
    private String oneDayPrior;

    @Column
    private String threeDaysPrior;

    @Column
    private String sixDaysPrior;

    @Column
    private String thirteenDaysPrior;

    @ManyToOne
    @JoinColumn(name = "PLANNER_ID", nullable = false)
    private Planner planner;

    @Builder
    public PlannerDetail(String studyDay, String learningStatus, String today, String oneDayPrior, String threeDaysPrior,
                         String sixDaysPrior, String thirteenDaysPrior, Planner planner) {
        this.studyDay = studyDay;
        this.learningStatus = learningStatus;
        this.today = today;
        this.oneDayPrior = oneDayPrior;
        this.threeDaysPrior = threeDaysPrior;
        this.sixDaysPrior = sixDaysPrior;
        this.thirteenDaysPrior = thirteenDaysPrior;
        this.planner = planner;
    }

    public void updatePlannerDetail(String studyDay, String learningStatus, String today, String oneDayPrior, String threeDaysPrior,
                               String sixDaysPrior, String thirteenDaysPrior) {
        this.studyDay = studyDay;
        this.learningStatus = learningStatus;
        this.today = today;
        this.oneDayPrior = oneDayPrior;
        this.threeDaysPrior = threeDaysPrior;
        this.sixDaysPrior = sixDaysPrior;
        this.thirteenDaysPrior = thirteenDaysPrior;
    }
}
