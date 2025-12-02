package br.com.scoa.controller;

import br.com.scoa.model.Disciplina;
import br.com.scoa.service.CursoService;
import br.com.scoa.service.DisciplinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/disciplinas")
public class DisciplinaPageController {

    private final DisciplinaService disciplinaService;
    private final CursoService cursoService;

    public DisciplinaPageController(DisciplinaService disciplinaService,
                                    CursoService cursoService) {
        this.disciplinaService = disciplinaService;
        this.cursoService = cursoService;
    }

    // LISTA
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("disciplinas", disciplinaService.listar());
        return "disciplinas/lista"; // templates/disciplinas/lista.html
    }

    // FORM NOVA DISCIPLINA
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("disciplina", new Disciplina());
        model.addAttribute("cursos", cursoService.listar()); // para o select
        return "disciplinas/form"; // templates/disciplinas/form.html
    }

    // SALVAR
    @PostMapping
    public String salvar(@ModelAttribute("disciplina") Disciplina disciplina,
                         @RequestParam("idCurso") Long idCurso) {

        disciplinaService.criar(idCurso, disciplina);
        return "redirect:/disciplinas";
    }
}
