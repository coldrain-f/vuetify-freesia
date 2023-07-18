package edu.coldrain.freesia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FreesiaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreesiaSpringApplication.class, args);
    }

}
