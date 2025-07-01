package com.autobots.automanager.controles;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private UsuarioRepositorio repositorio;

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable Long id) {
        Usuario usuario = repositorio.findById(id).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obterUsuarios() {
        List<Usuario> usuarios = repositorio.findAll();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getId() != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (!perfisValidos(usuario.getPerfis())) {
            return new ResponseEntity<>("Perfis inválidos fornecidos.", HttpStatus.BAD_REQUEST);
        }

        repositorio.save(usuario);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao) {
        if (atualizacao.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Usuario existente = repositorio.findById(atualizacao.getId()).orElse(null);
        if (existente != null) {
            if (!perfisValidos(atualizacao.getPerfis())) {
                return new ResponseEntity<>("Perfis inválidos fornecidos.", HttpStatus.BAD_REQUEST);
            }

            repositorio.delete(existente);
            atualizacao.setId(null);
            repositorio.save(atualizacao);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirUsuario(@RequestBody Usuario exclusao) {
        Usuario usuario = repositorio.findById(exclusao.getId()).orElse(null);
        if (usuario != null) {
            repositorio.delete(usuario);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean perfisValidos(Set<PerfilUsuario> perfis) {
        for (PerfilUsuario perfil : perfis) {
            if (perfil == null) return false;
            try {
                PerfilUsuario.valueOf(perfil.name());
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }

}
