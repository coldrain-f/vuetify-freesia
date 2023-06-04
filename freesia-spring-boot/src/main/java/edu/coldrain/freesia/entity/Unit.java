package edu.coldrain.freesia.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "UNIT_SEQ_GENERATOR", sequenceName = "UNIT_SEQ")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"SUBJECT", "VOCABULARY_ID"}))
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIT_SEQ_GENERATOR")
    @Column(name = "UNIT_ID")
    private Long id;

    @Column(nullable = false)
    private String subject;

    @ManyToOne
    @JoinColumn(name = "VOCABULARY_ID", nullable = false)
    private Vocabulary vocabulary;

    @Builder
    public Unit(String subject, Vocabulary vocabulary) {
        this.subject = subject;
        this.vocabulary = vocabulary;
    }

    public void changeSubject(String subject) {
        this.subject = subject;
    }
}