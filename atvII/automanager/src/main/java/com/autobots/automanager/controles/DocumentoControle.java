package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private DocumentoRepositorio repositorio;

	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;

	@GetMapping("/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
		Documento documento = repositorio.findById(id).orElse(null);
		if (documento == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(documento);
			return new ResponseEntity<>(documento, HttpStatus.OK);
		}
	}

	@GetMapping
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		if (documentos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(documentos);
			return new ResponseEntity<>(documentos, HttpStatus.OK);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento) {
		if (documento.getId() == null) {
			repositorio.save(documento);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento documento) {
		if (documento.getId() != null && repositorio.existsById(documento.getId())) {
			repositorio.save(documento);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirDocumento(@RequestBody Documento documento) {
		if (documento.getId() != null && repositorio.existsById(documento.getId())) {
			repositorio.deleteById(documento.getId());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
