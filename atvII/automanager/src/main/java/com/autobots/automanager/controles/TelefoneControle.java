package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private TelefoneRepositorio repositorio;

    @Autowired
    private AdicionadorLinkTelefone adicionadorLink;

    @GetMapping("/{id}")
    public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
        Telefone tel = repositorio.findById(id).orElse(null);
        if (tel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        adicionadorLink.adicionarLink(tel);
        return new ResponseEntity<>(tel, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Telefone>> obterTelefones() {
        List<Telefone> lista = repositorio.findAll();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        adicionadorLink.adicionarLink(lista);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone telefone) {
        if (telefone.getId() == null) {
            repositorio.save(telefone);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone telefone) {
        if (telefone.getId() != null && repositorio.existsById(telefone.getId())) {
            repositorio.save(telefone);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirTelefone(@RequestBody Telefone telefone) {
        if (telefone.getId() != null && repositorio.existsById(telefone.getId())) {
            repositorio.deleteById(telefone.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
