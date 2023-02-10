package com.foro.forordokotoro.Utils.request;

import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.Enumerations.EtypePublication;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class PublicationReçu {
    @Size(max = 60)
    private String titre;

    @Size(max = 60)
    private String soustitre;

    @Size(max = 255)
    private String description;

    private String typepub;

}

