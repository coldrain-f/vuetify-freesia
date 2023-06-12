package edu.coldrain.freesia.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UnitDTO {

    @Data
    public static class Response {

        private Long id;

        private String subject;

        private Long wordCount;

        @Builder
        @QueryProjection // new QUnitDTO_Response
        public Response(Long id, String subject, Long wordCount) {
            this.id = id;
            this.subject = subject;
            this.wordCount = wordCount;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegistrationRequest {

        private String subject;

        public RegistrationRequest(Long vocabularyId,  String subject) {
            this.subject = subject;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ModifyRequest {

        private String subject;

        public ModifyRequest(String subject) {
            this.subject = subject;
        }
    }
}
