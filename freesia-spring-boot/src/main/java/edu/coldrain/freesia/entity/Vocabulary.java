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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE", "LANGUAGE_ID"}))
public class Vocabulary extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOCABULARY_SEQ_GENERATOR")
    @Column(name = "VOCABULARY_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "LANGUAGE_ID", nullable = false)
    private Language language;

    @Builder
    public Vocabulary(String title, Language language) {
        this.title = title;
        this.language = language;
    }

    public void modifyVocabulary(String title, Language language) {
        this.title = title;
        this.language = language;
    }
}
