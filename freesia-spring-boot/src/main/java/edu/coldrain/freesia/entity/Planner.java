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

    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "VOCABULARY_ID")
    private Vocabulary vocabulary;

    @Builder
    public Planner(String name, Vocabulary vocabulary) {
        this.name = name;
        this.vocabulary = vocabulary;
    }

}
