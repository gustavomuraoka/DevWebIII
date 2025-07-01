package com.comunicacao.sistema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.comunicacao.sistema.entidades.Usuario;
import com.comunicacao.sistema.repositorios.RepositorioUsuario;

@SpringBootApplication
public class SistemaApplication implements CommandLineRunner {

	@Autowired
	private RepositorioUsuario repositorio;

	public static void main(String[] args) {
		SpringApplication.run(SistemaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 3; i++) {
			Usuario usuario = new Usuario();
			usuario.setNome("Usuario " + (i + 1));
			repositorio.save(usuario);
		}
	}
}