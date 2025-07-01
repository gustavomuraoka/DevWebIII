package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.entidades.Mercadoria;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria> {

    @Override
    public void adicionarLink(List<Mercadoria> lista) {
        for (Mercadoria mercadoria : lista) {
            long id = mercadoria.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(id))
                    .withSelfRel();
            mercadoria.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(Mercadoria mercadoria) {
        long id = mercadoria.getId();

        Link self = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(id))
                .withSelfRel();

        Link todas = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias())
                .withRel("mercadorias");

        Link atualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).atualizarMercadoria(mercadoria))
                .withRel("atualizar");

        Link excluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).excluirMercadoria(mercadoria))
                .withRel("excluir");

        mercadoria.add(self, todas, atualizar, excluir);
    }
}
