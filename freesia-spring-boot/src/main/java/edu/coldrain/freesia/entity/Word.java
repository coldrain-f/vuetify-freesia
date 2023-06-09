package edu.coldrain.freesia.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "WORD_SEQ_GENERATOR", sequenceName = "WORD_SEQ")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORD_SEQ_GENERATOR")
    @Column(name = "WORD_ID")
    private Long id;

    @Column(nullable = false)
    private String studyWord;

    @Column(nullable = false)
    private String nativeWord;

    @Column(nullable = false)
    private String partOfSpeech;

    @ManyToOne
    @JoinColumn(name = "UNIT_ID")
    private Unit unit;

    @Builder
    public Word(String studyWord, String nativeWord, String partOfSpeech, Unit unit) {
        this.studyWord = studyWord;
        this.nativeWord = nativeWord;
        this.partOfSpeech = partOfSpeech;
        this.unit = unit;
    }

    public void changeStudyWord(String studyWord) {
        this.studyWord = studyWord;
    }

    public void changeNativeWord(String nativeWord) {
        this.nativeWord = nativeWord;
    }

    public void changePartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }
}