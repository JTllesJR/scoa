package br.com.scoa.controller;

import br.com.scoa.model.Aluno;
import br.com.scoa.model.Matricula;
import br.com.scoa.service.AlunoService;
import br.com.scoa.service.MatriculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/boletim")
public class BoletimPageController {

    private final AlunoService alunoService;
    private final MatriculaService matriculaService;

    public BoletimPageController(AlunoService alunoService,
                                 MatriculaService matriculaService) {
        this.alunoService = alunoService;
        this.matriculaService = matriculaService;
    }

    // /boletim/aluno/{id}
    @GetMapping("/aluno/{alunoId}")
    public String boletimAluno(@PathVariable Long alunoId, Model model) {

        Aluno aluno = alunoService.buscarPorId(alunoId);
        List<Matricula> matriculas = matriculaService.listarPorAluno(alunoId);

        model.addAttribute("aluno", aluno);
        model.addAttribute("matriculas", matriculas);

        return "boletim/aluno";
    }
}
