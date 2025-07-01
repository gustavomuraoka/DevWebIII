package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Usuario;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario> {

    @Override
    public void adicionarLink(Usuario usuario) {
        Long id = usuario.getId();

        Link self = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(id))
                .withSelfRel();

        Link todos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios())
                .withRel("usuarios");

        Link atualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).atualizarUsuario(usuario))
                .withRel("atualizar");

        Link excluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).excluirUsuario(usuario))
                .withRel("excluir");

        usuario.add(self, todos, atualizar, excluir);
    }

    @Override
    public void adicionarLink(List<Usuario> lista) {
        for (Usuario usuario : lista) {
            adicionarLink(usuario);
        }
    }
}
