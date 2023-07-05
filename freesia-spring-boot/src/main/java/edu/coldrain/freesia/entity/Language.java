package edu.coldrain.freesia.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "LANGUAGE_SEQ_GENERATOR", sequenceName = "LANGUAGE_SEQ", allocationSize = 1)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LANGUAGE_SEQ_GENERATOR")
    @Column(name = "LANGUAGE_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder
    public Language(String name) {
        this.name = name;
    }
}
