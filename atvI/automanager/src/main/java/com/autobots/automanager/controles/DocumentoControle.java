package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

	@Autowired
	private DocumentoRepositorio repositorio;

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Autowired
	private DocumentoSelecionador selecionador;

	@GetMapping("/documento/{id}")
	public Documento obterDocumento(@PathVariable long id) {
		List<Documento> documentos = repositorio.findAll();
		return selecionador.selecionar(documentos, id);
	}

	@GetMapping("/documentos")
	public List<Documento> obterDocumentos() {
		return repositorio.findAll();
	}

	@PostMapping("/cadastro/{idCliente}")
	public void cadastrarDocumento(@PathVariable Long idCliente, @RequestBody Documento documento) {
		Cliente cliente = clienteRepositorio.findById(idCliente).orElse(null);
		if (cliente != null) {
			cliente.getDocumentos().add(documento);
			clienteRepositorio.save(cliente);
		}
	}

	@PutMapping("/atualizar")
	public void atualizarDocumento(@RequestBody Documento atualizacao) {
		Documento documento = repositorio.getById(atualizacao.getId());
		DocumentoAtualizador atualizador = new DocumentoAtualizador();
		atualizador.atualizar(documento, atualizacao);
		repositorio.save(documento);
	}

	@DeleteMapping("/excluir/{idCliente}")
	public void excluirDocumento(@PathVariable Long idCliente, @RequestBody Documento exclusao) {
		Cliente cliente = clienteRepositorio.findById(idCliente).orElse(null);
		if (cliente != null) {
			Documento documento = repositorio.getById(exclusao.getId());
			cliente.getDocumentos().removeIf(doc -> doc.getId().equals(documento.getId()));
			clienteRepositorio.save(cliente);
		}
	}
}
