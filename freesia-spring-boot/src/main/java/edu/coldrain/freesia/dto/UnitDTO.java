package edu.coldrain.freesia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class UnitDTO {

    @Data
    public static class Response {

        private Long id;

        private String subject;

        private Long wordCount;

        @JsonFormat(pattern = "yyyy. MM. dd. HH:mm")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy. MM. dd. HH:mm")
        private LocalDateTime modifiedAt;

        @Builder
        @QueryProjection // new QUnitDTO_Response
        public Response(Long id, String subject, Long wordCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.id = id;
            this.subject = subject;
            this.wordCount = wordCount;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
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
