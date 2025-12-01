package br.com.scoa.controller;

import br.com.scoa.model.Professor;
import br.com.scoa.service.ProfessorService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Professor> criar(@RequestBody Professor p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(p));
    }

    @GetMapping
    public List<Professor> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Professor buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
}
