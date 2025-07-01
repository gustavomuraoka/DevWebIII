package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.entidades.Documento;

@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<Documento> {

	@Override
	public void adicionarLink(List<Documento> lista) {
		for (Documento documento : lista) {
			this.adicionarLink(documento);
		}
	}

	@Override
	public void adicionarLink(Documento documento) {
		long id = documento.getId();

		Link self = WebMvcLinkBuilder
			.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(id))
			.withSelfRel();

		Link all = WebMvcLinkBuilder
			.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentos())
			.withRel("documentos");

		Link atualizar = WebMvcLinkBuilder
			.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).atualizarDocumento(documento))
			.withRel("atualizar");

		Link excluir = WebMvcLinkBuilder
			.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).excluirDocumento(documento))
			.withRel("excluir");

		documento.add(self, all, atualizar, excluir);
	}
}
