package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entidades.Cliente;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Cliente> {

	@Override
	public void adicionarLink(List<Cliente> lista) {
		for (Cliente cliente : lista) {
			long id = cliente.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ClienteControle.class)
							.obterCliente(id))
					.withSelfRel();
			cliente.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Cliente cliente) {
		long id = cliente.getId();

		// Link para obter esse cliente (GET)
		Link linkSelf = WebMvcLinkBuilder
			.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterCliente(id))
			.withSelfRel();

		// Link para obter todos os clientes (GET)
		Link linkClientes = WebMvcLinkBuilder
			.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterClientes())
			.withRel("clientes");

		// Link para atualizar esse cliente (PUT)
		Link linkAtualizar = WebMvcLinkBuilder
			.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).atualizarCliente(cliente))
			.withRel("atualizar");

		// Link para deletar esse cliente (DELETE)
		Link linkExcluir = WebMvcLinkBuilder
			.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).excluirCliente(cliente))
			.withRel("excluir");

		cliente.add(linkSelf, linkClientes, linkAtualizar, linkExcluir);
	}

}