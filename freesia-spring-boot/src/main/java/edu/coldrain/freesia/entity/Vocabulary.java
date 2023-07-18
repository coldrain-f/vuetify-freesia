package edu.coldrain.freesia.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "VOCABULARY_SEQ_GENERATOR", sequenceName = "VOCABULARY_SEQ")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE", "LANGUAGE_ID"}))
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOCABULARY_SEQ_GENERATOR")
    @Column(name = "VOCABULARY_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "LANGUAGE_ID", nullable = false)
    private Language language;

    @OneToOne
    @JoinColumn(name = "PLANNER_ID")
    private Planner planner;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

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
