package com.foro.forordokotoro.payload.request;

import lombok.Data;

@Data
public class DemandeTransporteur {

    private String numeroplaque;

    private Boolean disponibilite;
}