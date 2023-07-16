package edu.coldrain.freesia.dto;

import lombok.Builder;
import lombok.Data;

public class LanguageDTO {

    @Data
    public static class Response {
        private Long id;

        private String name;

        @Builder
        public Response(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
