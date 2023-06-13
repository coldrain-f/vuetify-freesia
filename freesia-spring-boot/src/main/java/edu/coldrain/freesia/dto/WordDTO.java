package edu.coldrain.freesia.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WordDTO {

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegistrationRequest {

        private String studyWord;

        private String nativeWord;

        private String partOfSpeech;

        public RegistrationRequest(String studyWord, String nativeWord, String partOfSpeech) {
            this.studyWord = studyWord;
            this.nativeWord = nativeWord;
            this.partOfSpeech = partOfSpeech;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ModifyRequest {

        private String studyWord;

        private String nativeWord;

        private String partOfSpeech;

        public ModifyRequest(String studyWord, String nativeWord, String partOfSpeech) {
            this.studyWord = studyWord;
            this.nativeWord = nativeWord;
            this.partOfSpeech = partOfSpeech;
        }
    }

    @Data
    public static class Response {

        private Long id;

        private String studyWord;

        private String nativeWord;

        private String partOfSpeech;

        @Builder
        @QueryProjection
        public Response(Long id, String studyWord, String nativeWord, String partOfSpeech) {
            this.id = id;
            this.studyWord = studyWord;
            this.nativeWord = nativeWord;
            this.partOfSpeech = partOfSpeech;
        }
    }
}
