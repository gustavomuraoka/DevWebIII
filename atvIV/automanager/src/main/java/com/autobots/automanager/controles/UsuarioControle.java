package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControle {

    @Autowired
    private UsuarioRepositorio repositorio;

    // CREATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        repositorio.save(usuario);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // READ ALL
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> obterUsuarios() {
        List<Usuario> usuarios = repositorio.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // READ BY ID
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = repositorio.findById(id);
        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getId() != null && repositorio.existsById(usuario.getId())) {
            repositorio.save(usuario);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuário não encontrado para atualização", HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirUsuario(@RequestBody Usuario usuario, Authentication authentication) {
        if (usuario.getId() == null || !repositorio.existsById(usuario.getId())) {
            return new ResponseEntity<>("Usuário não encontrado para exclusão", HttpStatus.NOT_FOUND);
        }

        Usuario alvo = repositorio.findById(usuario.getId()).get();
        List<String> papeis = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        if (papeis.contains("ROLE_ADMIN")) {
            repositorio.delete(alvo);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        if (papeis.contains("ROLE_GERENTE")) {
            if (contemPerfil(alvo, "ROLE_CLIENTE") ||
                contemPerfil(alvo, "ROLE_VENDEDOR") ||
                contemPerfil(alvo, "ROLE_GERENTE")) {
                repositorio.delete(alvo);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Gerente não pode excluir este tipo de usuário", HttpStatus.FORBIDDEN);
            }
        }

        if (papeis.contains("ROLE_VENDEDOR")) {
            if (contemPerfil(alvo, "ROLE_CLIENTE")) {
                repositorio.delete(alvo);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Vendedor só pode excluir clientes", HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>("Você não tem permissão para excluir", HttpStatus.FORBIDDEN);
    }

    private boolean contemPerfil(Usuario usuario, String perfil) {
        return usuario.getPerfis().stream()
            .map(Enum::name)
            .anyMatch(p -> ("ROLE_" + p).equals(perfil));
    }
}
