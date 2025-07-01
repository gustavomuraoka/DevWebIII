package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	@Autowired
	private EnderecoRepositorio repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
		Endereco endereco = repositorio.findById(id).orElse(null);
		if (endereco == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(endereco, HttpStatus.OK);
		}
	}

	@GetMapping
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = repositorio.findAll();
		if (enderecos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(enderecos, HttpStatus.OK);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco) {
		if (endereco.getId() == null) {
			repositorio.save(endereco);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco endereco) {
		if (endereco.getId() != null && repositorio.existsById(endereco.getId())) {
			repositorio.save(endereco);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEndereco(@RequestBody Endereco endereco) {
		if (endereco.getId() != null && repositorio.existsById(endereco.getId())) {
			repositorio.deleteById(endereco.getId());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
