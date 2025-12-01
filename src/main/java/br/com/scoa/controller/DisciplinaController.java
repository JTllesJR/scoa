package br.com.scoa.controller;

import br.com.scoa.model.Disciplina;
import br.com.scoa.service.DisciplinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    public DisciplinaController(DisciplinaService service) {
        this.service = service;
    }

    // Cadastrar disciplina em um curso específico
    // POST /disciplinas/curso/1
    @PostMapping("/curso/{idCurso}")
    public ResponseEntity<Disciplina> criar(@PathVariable Long idCurso,
                                            @RequestBody Disciplina disciplina) {
        Disciplina salva = service.criar(idCurso, disciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping
    public List<Disciplina> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Disciplina buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
