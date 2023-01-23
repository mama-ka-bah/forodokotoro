package com.foro.forordokotoro.Utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reponse {
    private String message;
    private  int status;
}
