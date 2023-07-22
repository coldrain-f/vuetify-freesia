package edu.coldrain.freesia.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "PLANNER_SEQ_GENERATOR", sequenceName = "PLANNER_SEQ", allocationSize = 1)
public class Planner extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLANNER_SEQ_GENERATOR")
    @Column(name = "PLANNER_ID")
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
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

    @Builder
    public Planner(String studyDay, String learningStatus) {
        this.studyDay = studyDay;
        this.learningStatus = learningStatus;
    }

    // 복습주기 설정 메서드
    public void setTheReviewInterval(String today, String oneDayPrior, String threeDaysPrior, String sixDaysPrior, String thirteenDaysPrior) {
        this.today = today;
        this.oneDayPrior = oneDayPrior;
        this.threeDaysPrior = threeDaysPrior;
        this.sixDaysPrior = sixDaysPrior;
        this.thirteenDaysPrior = thirteenDaysPrior;
    }

}
