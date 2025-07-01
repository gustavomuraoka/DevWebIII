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

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/clientes")
public class ClienteControle {

    @Autowired
    private ClienteRepositorio repositorio;

    // CREATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
        repositorio.save(cliente);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // READ ALL
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<Cliente>> obterClientes() {
        List<Cliente> clientes = repositorio.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    // READ BY ID
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obterCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = repositorio.findById(id);
        if (cliente.isPresent()) {
            return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarCliente(@RequestBody Cliente cliente) {
        if (cliente.getId() != null && repositorio.existsById(cliente.getId())) {
            repositorio.save(cliente);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cliente não encontrado para atualização", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirCliente(@RequestBody Cliente cliente, Authentication authentication) {
        if (cliente.getId() == null || !repositorio.existsById(cliente.getId())) {
            return new ResponseEntity<>("Cliente não encontrado para exclusão", HttpStatus.NOT_FOUND);
        }

        Cliente alvo = repositorio.findById(cliente.getId()).get();
        String usuarioLogado = authentication.getName();
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

    private boolean contemPerfil(Cliente cliente, String perfil) {
        return cliente.getPerfis().stream()
            .map(Enum::name)
            .anyMatch(p -> ("ROLE_" + p).equals(perfil));
    }

}
