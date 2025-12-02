package br.com.scoa.controller;

import br.com.scoa.model.Turma;
import br.com.scoa.service.TurmaService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    private final TurmaService service;

    public TurmaController(TurmaService service) {
        this.service = service;
    }

    @PostMapping("/disciplina/{idDisciplina}/professor/{idProfessor}")
    public ResponseEntity<Turma> criar(@PathVariable Long idDisciplina,
                                       @PathVariable Long idProfessor,
                                       @RequestBody Turma turma) {

        Turma salva = service.criar(idDisciplina, idProfessor, turma);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping
    public List<Turma> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Turma buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
