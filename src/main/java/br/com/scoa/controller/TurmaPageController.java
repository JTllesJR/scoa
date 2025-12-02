package br.com.scoa.controller;

import br.com.scoa.model.Turma;
import br.com.scoa.service.DisciplinaService;
import br.com.scoa.service.ProfessorService;
import br.com.scoa.service.TurmaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/turmas")
public class TurmaPageController {

    private final TurmaService turmaService;
    private final DisciplinaService disciplinaService;
    private final ProfessorService professorService;

    public TurmaPageController(TurmaService turmaService,
                               DisciplinaService disciplinaService,
                               ProfessorService professorService) {
        this.turmaService = turmaService;
        this.disciplinaService = disciplinaService;
        this.professorService = professorService;
    }

    // LISTA
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("turmas", turmaService.listar());
        return "turmas/lista"; // templates/turmas/lista.html
    }

    // FORM NOVA TURMA
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("turma", new Turma());
        model.addAttribute("disciplinas", disciplinaService.listar());
        model.addAttribute("professores", professorService.listar());
        return "turmas/form"; // templates/turmas/form.html
    }

    // SALVAR
    @PostMapping
    public String salvar(@ModelAttribute("turma") Turma turma,
                         @RequestParam("idDisciplina") Long idDisciplina,
                         @RequestParam("idProfessor") Long idProfessor) {

        turmaService.criar(idDisciplina, idProfessor, turma);
        return "redirect:/turmas";
    }
}
