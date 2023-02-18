package com.foro.forordokotoro.Utils.response;

import lombok.Data;

@Data
public class OtpResponse {
    private int code;

    private Long iduser;
    private String message;

    private Boolean validite;

    public OtpResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    private int status;

    public OtpResponse(int randomCode, String message, int status) {
        this.code = randomCode;
        this.message = message;
        this.status = status;
    }
}
