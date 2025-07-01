package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.entidades.Veiculo;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo> {

    @Override
    public void adicionarLink(List<Veiculo> lista) {
        for (Veiculo veiculo : lista) {
            long id = veiculo.getId();
            Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(id))
                .withSelfRel();
            veiculo.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(Veiculo veiculo) {
        long id = veiculo.getId();

        Link self = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(id))
            .withSelfRel();

        Link todos = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos())
            .withRel("veiculos");

        Link atualizar = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).atualizarVeiculo(veiculo))
            .withRel("atualizar");

        Link excluir = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).excluirVeiculo(veiculo))
            .withRel("excluir");

        veiculo.add(self, todos, atualizar, excluir);
    }
}
