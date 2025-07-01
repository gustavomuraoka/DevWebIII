package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.repositorios.ServicoRepositorio;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

    @Autowired
    private ServicoRepositorio repositorio;

    @GetMapping("/{id}")
    public ResponseEntity<Servico> obterServico(@PathVariable Long id) {
        Servico servico = repositorio.findById(id).orElse(null);
        return servico != null
            ? new ResponseEntity<>(servico, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Servico>> obterServicos() {
        List<Servico> servicos = repositorio.findAll();
        return servicos.isEmpty()
            ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
            : new ResponseEntity<>(servicos, HttpStatus.OK);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarServico(@RequestBody Servico servico) {
        if (servico.getId() == null) {
            repositorio.save(servico);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarServico(@RequestBody Servico atualizacao) {
        Servico servico = repositorio.findById(atualizacao.getId()).orElse(null);
        if (servico != null) {
            repositorio.save(atualizacao);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirServico(@RequestBody Servico exclusao) {
        Servico servico = repositorio.findById(exclusao.getId()).orElse(null);
        if (servico != null) {
            repositorio.delete(servico);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
