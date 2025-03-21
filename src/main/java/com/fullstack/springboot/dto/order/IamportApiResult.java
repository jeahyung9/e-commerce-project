package com.fullstack.springboot.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class IamportApiResult {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("response")
    private IamportResponseDTO response;
}