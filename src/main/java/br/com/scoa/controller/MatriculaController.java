package br.com.scoa.controller;

import br.com.scoa.model.Matricula;
import br.com.scoa.service.MatriculaService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService service;

    public MatriculaController(MatriculaService service) {
        this.service = service;
    }

    @PostMapping("/{alunoId}/{turmaId}")
    public ResponseEntity<Matricula> matricular(@PathVariable Long alunoId,
                                                @PathVariable Long turmaId) {
        Matricula matricula = service.matricular(alunoId, turmaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(matricula);
    }
}
