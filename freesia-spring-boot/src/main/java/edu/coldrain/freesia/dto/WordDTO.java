package edu.coldrain.freesia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class WordDTO {

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegistrationRequest {

        private String studyWord;

        private String nativeWord;

        private String furigana;

        private String partOfSpeech;

        public RegistrationRequest(String studyWord, String nativeWord, String furigana, String partOfSpeech) {
            this.studyWord = studyWord;
            this.nativeWord = nativeWord;
            this.furigana = furigana;
            this.partOfSpeech = partOfSpeech;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ModifyRequest {

        private String studyWord;

        private String nativeWord;

        private String furigana;

        private String partOfSpeech;

        public ModifyRequest(String studyWord, String nativeWord, String partOfSpeech, String furigana) {
            this.studyWord = studyWord;
            this.nativeWord = nativeWord;
            this.furigana = furigana;
            this.partOfSpeech = partOfSpeech;
        }
    }

    @Data
    public static class LearningWordParams {

        private List<String> unitList;

        public LearningWordParams(List<String> unitList) {
            this.unitList = unitList;
        }
    }

    @Data
    public static class Response {

        private Long id;

        private String studyWord;

        private String nativeWord;

        private String furigana;

        private String partOfSpeech;

        @JsonFormat(pattern = "yyyy. MM. dd. HH:mm")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy. MM. dd. HH:mm")
        private LocalDateTime modifiedAt;

        @Builder
        @QueryProjection
        public Response(Long id, String studyWord, String nativeWord, String furigana, String partOfSpeech, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.id = id;
            this.studyWord = studyWord;
            this.nativeWord = nativeWord;
            this.furigana = furigana;
            this.partOfSpeech = partOfSpeech;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }



    @Data
    public static class LearningWordResponse {
        private String studyWord;
        private String nativeWord;
        private String furigana;
        private String partOfSpeech;

        @QueryProjection
        public LearningWordResponse(String studyWord, String nativeWord, String furigana, String partOfSpeech) {
            this.studyWord = studyWord;
            this.nativeWord = nativeWord;
            this.furigana = furigana;
            this.partOfSpeech = partOfSpeech;
        }
    }
}
