package edu.coldrain.freesia.dto;

import lombok.*;

public class VocabularyDTO {

    @Data
    public static class RegistrationRequest {

        private String title;

        private String language;

        public RegistrationRequest(String title, String language) {
            this.title = title;
            this.language = language;
        }
    }

    @Data
    public static class Response {

        private String title;

        private String language;

        @Builder
        public Response(String title, String language) {
            this.title = title;
            this.language = language;
        }
    }
}
