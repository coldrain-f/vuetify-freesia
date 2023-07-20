package edu.coldrain.freesia.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "LEARNER_SEQ_GENERATOR", sequenceName = "LEARNER_SEQ", allocationSize = 1)
public class Learner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LEARNER_SEQ_GENERATOR")
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder
    public Learner(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
