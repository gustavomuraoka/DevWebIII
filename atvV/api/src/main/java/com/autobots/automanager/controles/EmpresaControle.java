package com.autobots.automanager.controles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.EmpresaRepositorio;


@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

    @Autowired
    private EmpresaRepositorio repositorio;

    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
        Empresa empresa = repositorio.findById(id).orElse(null);
        if (empresa == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(empresa);
            return new ResponseEntity<>(empresa, HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> obterEmpresas() {
        List<Empresa> empresas = repositorio.findAll();
        if (empresas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(empresas);
            return new ResponseEntity<>(empresas, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}/clientes")
    public ResponseEntity<List<Usuario>> obterClientesDaEmpresa(@PathVariable long id) {
        Empresa empresa = repositorio.findById(id).orElse(null);
        if (empresa == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Filtra só quem tem ROLE_CLIENTE
        List<Usuario> clientes = empresa.getUsuarios().stream()
            .filter(u -> u.getPerfis().contains(Perfil.ROLE_CLIENTE))
            .collect(Collectors.toList());

        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{id}/funcionarios")
    public ResponseEntity<List<Usuario>> obterFuncionariosDaEmpresa(@PathVariable long id) {
        Empresa empresa = repositorio.findById(id).orElse(null);
        if (empresa == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Considera funcionário quem NÃO é apenas cliente
        List<Usuario> funcionarios = empresa.getUsuarios().stream()
            .filter(u -> u.getPerfis().stream()
                          .anyMatch(p -> p != Perfil.ROLE_CLIENTE))
            .collect(Collectors.toList());

        return new ResponseEntity<>(funcionarios, HttpStatus.OK);
    }

    @GetMapping("/{id}/mercadorias")
    public ResponseEntity<Set<Mercadoria>> obterMercadoriasDaEmpresa(@PathVariable long id) {
        Empresa empresa = repositorio.findById(id).orElse(null);

        if (empresa == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Set<Mercadoria> mercadorias = empresa.getMercadorias();
        return new ResponseEntity<>(mercadorias, HttpStatus.OK);
    }

    @GetMapping("/{id}/servicos")
    public ResponseEntity<Set<Servico>> obterServicosDaEmpresa(@PathVariable long id) {
        Empresa empresa = repositorio.findById(id).orElse(null);

        if (empresa == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Set<Servico> servicos = empresa.getServicos();

        return new ResponseEntity<>(servicos, HttpStatus.OK);
    }

     @GetMapping("/{id}/veiculos")
    public ResponseEntity<Set<Veiculo>> obterVeiculosAtendidos(@PathVariable long id) {
        Empresa empresa = repositorio.findById(id).orElse(null);

        if (empresa == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Set<Veiculo> veiculos = new HashSet<>();

        for (Usuario usuario : empresa.getUsuarios()) {
            if (usuario.getVeiculos() != null) {
                veiculos.addAll(usuario.getVeiculos());
            }
        }

        return new ResponseEntity<>(veiculos, HttpStatus.OK);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa) {
        if (empresa.getId() == null) {
            repositorio.save(empresa);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarEmpresa(@RequestBody Empresa atualizacao) {
        if (atualizacao.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Empresa existente = repositorio.findById(atualizacao.getId()).orElse(null);
        if (existente != null) {
            repositorio.delete(existente);
            atualizacao.setId(null);
            repositorio.save(atualizacao); 
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirEmpresa(@RequestBody Empresa exclusao) {
        Empresa empresa = repositorio.findById(exclusao.getId()).orElse(null);
        if (empresa != null) {
            repositorio.delete(empresa);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

