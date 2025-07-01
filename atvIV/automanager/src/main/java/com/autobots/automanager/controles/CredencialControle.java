package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.*;
import com.autobots.automanager.repositorios.*;

@RestController
@RequestMapping("/credencial")
public class CredencialControle {

    @Autowired
    private CredencialUsuarioSenhaRepositorio usuarioSenhaRepo;

    @Autowired
    private CredencialCodigoBarraRepositorio codigoBarraRepo;

    // ----- CredencialUsuarioSenha -----
    @GetMapping("/usuario-senha/{id}")
    public ResponseEntity<CredencialUsuarioSenha> obterUsuarioSenha(@PathVariable Long id) {
        var credencial = usuarioSenhaRepo.findById(id).orElse(null);
        return credencial != null
            ? new ResponseEntity<>(credencial, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/usuario-senha")
    public ResponseEntity<List<CredencialUsuarioSenha>> obterTodasUsuarioSenha() {
        var lista = usuarioSenhaRepo.findAll();
        return lista.isEmpty()
            ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
            : new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping("/usuario-senha/cadastro")
    public ResponseEntity<?> cadastrarUsuarioSenha(@RequestBody CredencialUsuarioSenha credencial) {
        if (credencial.getId() == null) {
            usuarioSenhaRepo.save(credencial);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/usuario-senha/atualizar")
    public ResponseEntity<?> atualizarUsuarioSenha(@RequestBody CredencialUsuarioSenha atualizacao) {
        var existente = usuarioSenhaRepo.findById(atualizacao.getId()).orElse(null);
        if (existente != null) {
            usuarioSenhaRepo.save(atualizacao);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/usuario-senha/excluir")
    public ResponseEntity<?> excluirUsuarioSenha(@RequestBody CredencialUsuarioSenha exclusao) {
        var existente = usuarioSenhaRepo.findById(exclusao.getId()).orElse(null);
        if (existente != null) {
            usuarioSenhaRepo.delete(existente);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // ----- CredencialCodigoBarra -----
    @GetMapping("/codigo-barra/{id}")
    public ResponseEntity<CredencialCodigoBarra> obterCodigoBarra(@PathVariable Long id) {
        var credencial = codigoBarraRepo.findById(id).orElse(null);
        return credencial != null
            ? new ResponseEntity<>(credencial, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/codigo-barra")
    public ResponseEntity<List<CredencialCodigoBarra>> obterTodasCodigoBarra() {
        var lista = codigoBarraRepo.findAll();
        return lista.isEmpty()
            ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
            : new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping("/codigo-barra/cadastro")
    public ResponseEntity<?> cadastrarCodigoBarra(@RequestBody CredencialCodigoBarra credencial) {
        if (credencial.getId() == null) {
            codigoBarraRepo.save(credencial);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/codigo-barra/atualizar")
    public ResponseEntity<?> atualizarCodigoBarra(@RequestBody CredencialCodigoBarra atualizacao) {
        var existente = codigoBarraRepo.findById(atualizacao.getId()).orElse(null);
        if (existente != null) {
            codigoBarraRepo.save(atualizacao);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/codigo-barra/excluir")
    public ResponseEntity<?> excluirCodigoBarra(@RequestBody CredencialCodigoBarra exclusao) {
        var existente = codigoBarraRepo.findById(exclusao.getId()).orElse(null);
        if (existente != null) {
            codigoBarraRepo.delete(existente);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
