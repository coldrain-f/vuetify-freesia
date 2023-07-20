package edu.coldrain.freesia.dto;

import lombok.Data;

public class LearnerDTO {

    @Data
    public static class LoginRequest {

        private String email;

        private String password;
    }
}
