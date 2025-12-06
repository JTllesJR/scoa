package br.com.scoa.controller;

import br.com.scoa.model.Matricula;
import br.com.scoa.service.AlunoService;
import br.com.scoa.service.MatriculaService;
import br.com.scoa.service.TurmaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/matriculas")
public class MatriculaPageController {

    private final AlunoService alunoService;
    private final TurmaService turmaService;
    private final MatriculaService matriculaService;

    public MatriculaPageController(AlunoService alunoService,
                                   TurmaService turmaService,
                                   MatriculaService matriculaService) {
        this.alunoService = alunoService;
        this.turmaService = turmaService;
        this.matriculaService = matriculaService;
    }

    // LISTAGEM: GET /matriculas
    @GetMapping
    public String listar(Model model) {
        List<Matricula> matriculas = matriculaService.listar();
        model.addAttribute("matriculas", matriculas);
        return "matriculas/lista"; // templates/matriculas/lista.html
    }

    // FORM NOVO: GET /matriculas/novo
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("turmas", turmaService.listar());
        return "matriculas/form"; // templates/matriculas/form.html
    }

    // SALVAR: POST /matriculas
    @PostMapping
    public String salvar(@RequestParam("alunoId") Long alunoId,
                         @RequestParam("turmaId") Long turmaId,
                         @RequestParam("semestre") String semestre,
                         Model model) {

        try {
            // tenta efetuar a matrícula (com todas as regras de negócio)
            matriculaService.matricular(alunoId, turmaId, semestre);
            return "redirect:/matriculas";

        } catch (RuntimeException e) {
            // se der erro de regra de negócio, volta para o formulário
            // exibindo a mensagem para o usuário

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("alunos", alunoService.listarTodos());
            model.addAttribute("turmas", turmaService.listar());

            return "matriculas/form";
        }
    }
}
