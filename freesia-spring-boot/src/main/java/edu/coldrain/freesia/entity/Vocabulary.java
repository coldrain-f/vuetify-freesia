package edu.coldrain.freesia.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "VOCABULARY_SEQ_GENERATOR", sequenceName = "VOCABULARY_SEQ")
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOCABULARY_SEQ_GENERATOR")
    @Column(name = "VOCABULARY_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "LANGUAGE_ID", nullable = false, updatable = false)
    private Language language;

    @OneToOne
    @JoinColumn(name = "PLANNER_ID")
    private Planner planner;

    @Builder
    public Vocabulary(String title, Language language) {
        this.title = title;
        this.language = language;
    }

    public void changeTitle(String title) {
        this.title = title;
    }
}
