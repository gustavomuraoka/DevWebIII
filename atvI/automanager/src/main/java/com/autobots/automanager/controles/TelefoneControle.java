package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

	@Autowired
	private TelefoneRepositorio repositorio;

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Autowired
	private TelefoneSelecionador selecionador;

	@GetMapping("/telefone/{id}")
	public Telefone obterTelefone(@PathVariable long id) {
		List<Telefone> telefones = repositorio.findAll();
		return selecionador.selecionar(telefones, id);
	}

	@GetMapping("/telefones")
	public List<Telefone> obterTelefones() {
		return repositorio.findAll();
	}

	@PostMapping("/cadastro/{idCliente}")
	public void cadastrarTelefone(@PathVariable Long idCliente, @RequestBody Telefone telefone) {
		Cliente cliente = clienteRepositorio.findById(idCliente).orElse(null);
		if (cliente != null) {
			cliente.getTelefones().add(telefone);
			clienteRepositorio.save(cliente);
		}
	}

	@PutMapping("/atualizar")
	public void atualizarTelefone(@RequestBody Telefone atualizacao) {
		Telefone telefone = repositorio.getById(atualizacao.getId());
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefone, atualizacao);
		repositorio.save(telefone);
	}

	@DeleteMapping("/excluir/{idCliente}")
	public void excluirTelefone(@PathVariable Long idCliente, @RequestBody Telefone exclusao) {
		Cliente cliente = clienteRepositorio.findById(idCliente).orElse(null);
		if (cliente != null) {
			Telefone telefone = repositorio.getById(exclusao.getId());
			cliente.getTelefones().removeIf(tel -> tel.getId().equals(telefone.getId()));
			clienteRepositorio.save(cliente);
		}
	}
}
