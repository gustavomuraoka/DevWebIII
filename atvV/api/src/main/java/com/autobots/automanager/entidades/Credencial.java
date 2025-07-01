package com.autobots.automanager.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Credencial implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nomeUsuario;

    @Column(nullable = false)
    private String senha;
}
