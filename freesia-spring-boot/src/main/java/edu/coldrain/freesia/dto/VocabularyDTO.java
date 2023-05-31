package edu.coldrain.freesia.dto;

import lombok.Builder;
import lombok.Data;

public class VocabularyDTO {

    @Data
    public class RegistrationRequest {

        private String title;

        private String language;

        public RegistrationRequest(String title, String language) {
            this.title = title;
            this.language = language;
        }
    }

    @Data
    public class Response {

        private String title;

        private String language;

        @Builder
        public Response(String title, String language) {
            this.title = title;
            this.language = language;
        }
    }
}
