package com.comunicacao.sistema.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comunicacao.sistema.entidades.Usuario;
import com.comunicacao.sistema.repositorios.RepositorioUsuario;

@RestController
public class ControleUsuario {
	@Autowired
	private RepositorioUsuario repositorio;

	@GetMapping("/usuarios")
	public ResponseEntity<?> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		return new ResponseEntity<>(usuarios, HttpStatus.FOUND);
	}
}