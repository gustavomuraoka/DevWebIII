package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController
@RequestMapping("/venda")
public class VendaControle {

    @Autowired
    private VendaRepositorio repositorio;

    @Autowired
    private AdicionadorLinkVenda adicionadorLink;

    @PreAuthorize("hasAnyRole('GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obterVenda(@PathVariable Long id) {
        Venda venda = repositorio.findById(id).orElse(null);
        if (venda == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String username = getCurrentUsername();

        // GERENTE vê tudo
        if (hasRole("GERENTE")) {
            adicionadorLink.adicionarLink(venda);
            return new ResponseEntity<>(venda, HttpStatus.OK);
        }

        // VENDEDOR vê somente se for o autor da venda
        if (hasRole("VENDEDOR")) {
            if (venda.getFuncionario() != null &&
                venda.getFuncionario().getCredencial().getNomeUsuario().equals(username)) {
                adicionadorLink.adicionarLink(venda);
                return new ResponseEntity<>(venda, HttpStatus.OK);
            }
        }

        // CLIENTE vê se é o cliente da venda
        if (hasRole("CLIENTE")) {
            if (venda.getCliente() != null &&
                venda.getCliente().getCredencial().getNomeUsuario().equals(username)) {
                adicionadorLink.adicionarLink(venda);
                return new ResponseEntity<>(venda, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    // READ ALL (vendedor vê apenas as suas)
    @PreAuthorize("hasAnyRole('GERENTE', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<Venda>> obterVendas() {
        List<Venda> vendas;

        if (hasRole("GERENTE")) {
            vendas = repositorio.findAll();
        } else {
            String username = getCurrentUsername();
            vendas = repositorio.findAll().stream()
                .filter(v -> v.getFuncionario().getCredencial().getNomeUsuario().equals(username))
                .collect(Collectors.toList());
        }

        if (!vendas.isEmpty()) {
            adicionadorLink.adicionarLink(vendas);
            return new ResponseEntity<>(vendas, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // CREATE
    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda) {
        if (venda.getId() == null) {
            repositorio.save(venda);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    // UPDATE
    @PreAuthorize("hasRole('GERENTE')")
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarVenda(@RequestBody Venda atualizacao) {
        Venda existente = repositorio.findById(atualizacao.getId()).orElse(null);
        if (existente != null) {
            repositorio.delete(existente);
            atualizacao.setId(null);
            repositorio.save(atualizacao);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // DELETE
    @PreAuthorize("hasRole('GERENTE')")
    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirVenda(@RequestBody Venda exclusao) {
        Venda venda = repositorio.findById(exclusao.getId()).orElse(null);
        if (venda != null) {
            repositorio.delete(venda);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // === UTILITÁRIOS ===

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.equals("ROLE_" + role));
    }
}

