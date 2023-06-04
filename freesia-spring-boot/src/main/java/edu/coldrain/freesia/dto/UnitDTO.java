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

        @Builder
        @QueryProjection // new QUnitDTO_Response
        public Response(Long id, String subject) {
            this.id = id;
            this.subject = subject;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegistrationRequest {

        private Long vocabularyId;

        private String subject;

        public RegistrationRequest(Long vocabularyId,  String subject) {
            this.vocabularyId = vocabularyId;
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
