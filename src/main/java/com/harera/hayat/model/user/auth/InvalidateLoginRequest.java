package com.harera.hayat.model.user.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InvalidateLoginRequest {

    @JsonProperty("token")
    private String token;

    @JsonProperty("device_token")
    private String refreshToken;
}
