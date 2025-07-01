package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.entidades.Endereco;

@Component
public class AdicionadorLinkEndereco implements AdicionadorLink<Endereco> {

	@Override
	public void adicionarLink(List<Endereco> lista) {
		for (Endereco endereco : lista) {
			this.adicionarLink(endereco);
		}
	}

	@Override
	public void adicionarLink(Endereco endereco) {
		long id = endereco.getId();

		Link self = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEndereco(id))
				.withSelfRel();

		Link all = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecos())
				.withRel("enderecos");

		Link atualizar = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(EnderecoControle.class).atualizarEndereco(endereco))
				.withRel("atualizar");

		Link excluir = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(EnderecoControle.class).excluirEndereco(endereco))
				.withRel("excluir");

		endereco.add(self, all, atualizar, excluir);
	}
}
