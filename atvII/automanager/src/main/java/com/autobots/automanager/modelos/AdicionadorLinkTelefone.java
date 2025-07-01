package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.entidades.Telefone;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone> {

    @Override
    public void adicionarLink(List<Telefone> lista) {
        for (Telefone telefone : lista) {
            adicionarLink(telefone);
        }
    }

    @Override
    public void adicionarLink(Telefone telefone) {
        long id = telefone.getId();

        Link self = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefone(id)
        ).withSelfRel();

        Link all = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefones()
        ).withRel("telefones");

        Link atualizar = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(TelefoneControle.class).atualizarTelefone(telefone)
        ).withRel("atualizar");

        Link excluir = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(TelefoneControle.class).excluirTelefone(telefone)
        ).withRel("excluir");

        telefone.add(self, all, atualizar, excluir);
    }
}
