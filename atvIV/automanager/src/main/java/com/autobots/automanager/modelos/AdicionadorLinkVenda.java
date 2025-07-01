package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.entidades.Venda;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<Venda> {

    @Override
    public void adicionarLink(List<Venda> lista) {
        for (Venda venda : lista) {
            long id = venda.getId();
            Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(id))
                .withSelfRel();
            venda.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(Venda venda) {
        long id = venda.getId();

        Link self = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(id))
            .withSelfRel();

        Link todas = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas())
            .withRel("vendas");

        Link atualizar = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).atualizarVenda(venda))
            .withRel("atualizar");

        Link excluir = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).excluirVenda(venda))
            .withRel("excluir");

        venda.add(self, todas, atualizar, excluir);
    }
}
