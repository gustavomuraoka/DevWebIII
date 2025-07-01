package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entidades.Empresa;

@Component
public class AdicionadorLinkEmpresa {

    public void adicionarLink(Empresa empresa) {
        long id = empresa.getId();

        Link selfLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(id))
            .withSelfRel();

        Link todasEmpresas = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas())
            .withRel("empresas");

        Link atualizar = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).atualizarEmpresa(empresa))
            .withRel("atualizar");

        Link excluir = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).excluirEmpresa(empresa))
            .withRel("excluir");

        empresa.add(selfLink, todasEmpresas, atualizar, excluir);
    }

    public void adicionarLink(List<Empresa> empresas) {
        for (Empresa empresa : empresas) {
            adicionarLink(empresa);
        }
    }
}
