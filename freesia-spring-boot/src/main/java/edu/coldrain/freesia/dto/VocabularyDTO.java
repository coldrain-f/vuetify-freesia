package edu.coldrain.freesia.dto;

import com.querydsl.core.annotations.QueryProjection;
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
    @NoArgsConstructor(access = AccessLevel.PROTECTED) // // (no delegate- or property-based creator)
    public static class ModifyRequest {

        private String title;

        public ModifyRequest(String title) {
            this.title = title;
        }
    }

    @Data
    public static class Response {

        private Long id;
        private String title;
        private String language;

        private Long subunit;

        @Builder
        @QueryProjection // new QVocabularyDTO_Response
        public Response(Long id, String title, String language, Long subunit) {
            this.id = id;
            this.title = title;
            this.language = language;
            this.subunit = subunit;
        }
    }
}
