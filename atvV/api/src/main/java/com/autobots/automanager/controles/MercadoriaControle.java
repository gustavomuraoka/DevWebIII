package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelos.AdicionadorLinkMercadoria;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

    @Autowired
    private MercadoriaRepositorio repositorio;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorLink;

    // READ BY ID
    @PreAuthorize("hasAnyRole('GERENTE', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable Long id) {
        Mercadoria mercadoria = repositorio.findById(id).orElse(null);
        if (mercadoria != null) {
            adicionadorLink.adicionarLink(mercadoria);
            return new ResponseEntity<>(mercadoria, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // READ ALL
    @PreAuthorize("hasAnyRole('GERENTE', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<Mercadoria>> obterMercadorias() {
        List<Mercadoria> mercadorias = repositorio.findAll();
        if (!mercadorias.isEmpty()) {
            adicionadorLink.adicionarLink(mercadorias);
            return new ResponseEntity<>(mercadorias, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // CREATE
    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
        if (mercadoria.getId() == null) {
            repositorio.save(mercadoria);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    // UPDATE
    @PreAuthorize("hasRole('GERENTE')")
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarMercadoria(@RequestBody Mercadoria atualizacao) {
        Mercadoria existente = repositorio.findById(atualizacao.getId()).orElse(null);
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
    public ResponseEntity<?> excluirMercadoria(@RequestBody Mercadoria exclusao) {
        Mercadoria mercadoria = repositorio.findById(exclusao.getId()).orElse(null);
        if (mercadoria != null) {
            repositorio.delete(mercadoria);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

