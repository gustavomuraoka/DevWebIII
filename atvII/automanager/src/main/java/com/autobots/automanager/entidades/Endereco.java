package com.autobots.automanager.entidades;

import javax.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import lombok.Data;

@Data
@Entity
public class Endereco extends RepresentationModel<Endereco> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	private String estado;

	@Column(nullable = false)
	private String cidade;

	@Column(nullable = true)
	private String bairro;

	@Column(nullable = false)
	private String rua;

	@Column(nullable = false)
	private String numero;

	@Column(nullable = true)
	private String codigoPostal;

	@Column(nullable = true)
	private String informacoesAdicionais;
}
