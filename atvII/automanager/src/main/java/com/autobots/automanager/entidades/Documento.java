package com.autobots.automanager.entidades;

import javax.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import lombok.Data;

@Data
@Entity
public class Documento extends RepresentationModel<Documento> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String tipo;

	@Column(unique = true)
	private String numero;
}
