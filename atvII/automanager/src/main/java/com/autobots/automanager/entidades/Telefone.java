package com.autobots.automanager.entidades;

import javax.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import lombok.Data;

@Data
@Entity
public class Telefone extends RepresentationModel<Telefone> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String ddd;

    @Column
    private String numero;
}
