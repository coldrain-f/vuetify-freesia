package edu.coldrain.freesia;

import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.coldrain.freesia.entity.Language;
import edu.coldrain.freesia.entity.QLanguage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FreesiaSpringApplicationTests {

    @PersistenceContext
    private EntityManager em;

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("QueryDSL 동작 테스트")
    void querydslTest() {
        // 1.
        JPAQueryFactory query = new JPAQueryFactory(em);

        // 2.
        QLanguage qLanguage = QLanguage.language;

        // 3.
        Language language = query.selectFrom(qLanguage)
                .orderBy(qLanguage.id.asc())
                .fetchFirst();

        // 4.
        assertThat(language.getName()).isEqualTo("English");
    }

}
