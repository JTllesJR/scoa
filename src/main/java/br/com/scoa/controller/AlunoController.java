package br.com.scoa.controller;

import br.com.scoa.model.Aluno;
import br.com.scoa.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Aluno> criar(@RequestBody Aluno aluno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarAluno(aluno));
    }

    @GetMapping
    public List<Aluno> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Aluno buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
