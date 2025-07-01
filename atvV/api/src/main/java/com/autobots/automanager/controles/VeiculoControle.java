package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.AdicionadorLinkVeiculo;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

    @Autowired
    private VeiculoRepositorio repositorio;

    @Autowired
    private AdicionadorLinkVeiculo adicionadorLink;

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> obterVeiculo(@PathVariable Long id) {
        Veiculo veiculo = repositorio.findById(id).orElse(null);
        if (veiculo != null) {
            adicionadorLink.adicionarLink(veiculo);
            return new ResponseEntity<>(veiculo, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Veiculo>> obterVeiculos() {
        List<Veiculo> veiculos = repositorio.findAll();
        if (!veiculos.isEmpty()) {
            adicionadorLink.adicionarLink(veiculos);
            return new ResponseEntity<>(veiculos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
        if (veiculo.getId() == null) {
            repositorio.save(veiculo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarVeiculo(@RequestBody Veiculo atualizacao) {
        Veiculo existente = repositorio.findById(atualizacao.getId()).orElse(null);
        if (existente != null) {
            repositorio.delete(existente);
            atualizacao.setId(null);
            repositorio.save(atualizacao);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirVeiculo(@RequestBody Veiculo exclusao) {
        Veiculo veiculo = repositorio.findById(exclusao.getId()).orElse(null);
        if (veiculo != null) {
            repositorio.delete(veiculo);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
