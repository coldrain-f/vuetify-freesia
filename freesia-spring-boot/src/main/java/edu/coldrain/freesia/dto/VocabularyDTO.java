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
    public static class ModifyRequest {

        private String title;

        @Builder
        public ModifyRequest(String title) {
            this.title = title;
        }
    }

    @Data
    public static class Response {

        private Long id;
        private String title;

        private String language;

        @Builder
        public Response(Long id, String title, String language) {
            this.id = id;
            this.title = title;
            this.language = language;
        }
    }
}
