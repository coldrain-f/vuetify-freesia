package edu.coldrain.freesia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

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
    @NoArgsConstructor(access = AccessLevel.PROTECTED) // // (no delegate- or property-based creator)
    public static class ModifyRequest {

        private String title;

        private String language;

        public ModifyRequest(String title, String language) {
            this.title = title;
            this.language = language;
        }
    }

    @Data
    public static class Response {

        private Long id;
        private String title;
        private String language;
        private Long unitCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime modifiedAt;


        @Builder
        @QueryProjection // new QVocabularyDTO_Response
        public Response(Long id, String title, String language, Long unitCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.id = id;
            this.title = title;
            this.language = language;
            this.unitCount = unitCount;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }
}
