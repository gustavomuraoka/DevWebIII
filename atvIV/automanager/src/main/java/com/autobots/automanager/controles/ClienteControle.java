package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/clientes")
public class ClienteControle {

    @Autowired
    private ClienteRepositorio repositorio;

    // CREATE
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
        repositorio.save(cliente);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping("/listar")
    public ResponseEntity<List<Cliente>> obterClientes() {
        List<Cliente> clientes = repositorio.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    // READ BY ID
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
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarCliente(@RequestBody Cliente cliente) {
        if (cliente.getId() != null && repositorio.existsById(cliente.getId())) {
            repositorio.save(cliente);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cliente não encontrado para atualização", HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirCliente(@RequestBody Cliente cliente) {
        if (cliente.getId() == null || !repositorio.existsById(cliente.getId())) {
            return new ResponseEntity<>("Cliente não encontrado para exclusão", HttpStatus.NOT_FOUND);
        }

        Cliente alvo = repositorio.findById(cliente.getId()).get();
        repositorio.delete(alvo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
