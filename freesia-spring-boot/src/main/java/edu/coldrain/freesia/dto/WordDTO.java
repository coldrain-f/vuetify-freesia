package edu.coldrain.freesia.dto;

import lombok.AccessLevel;
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
}
