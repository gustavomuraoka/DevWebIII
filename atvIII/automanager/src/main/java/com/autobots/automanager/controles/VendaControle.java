package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<Venda> obterVenda(@PathVariable Long id) {
        Venda venda = repositorio.findById(id).orElse(null);
        if (venda != null) {
            adicionadorLink.adicionarLink(venda);
            return new ResponseEntity<>(venda, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Venda>> obterVendas() {
        List<Venda> vendas = repositorio.findAll();
        if (!vendas.isEmpty()) {
            adicionadorLink.adicionarLink(vendas);
            return new ResponseEntity<>(vendas, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda) {
        if (venda.getId() == null) {
            repositorio.save(venda);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

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

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirVenda(@RequestBody Venda exclusao) {
        Venda venda = repositorio.findById(exclusao.getId()).orElse(null);
        if (venda != null) {
            repositorio.delete(venda);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
