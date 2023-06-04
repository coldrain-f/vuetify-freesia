package edu.coldrain.freesia.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

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
}
