package br.com.scoa.controller;

import br.com.scoa.model.Curso;
import br.com.scoa.service.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cursos") // 👈 /cursos é interface HTML
public class CursoPageController {

    private final CursoService service;

    public CursoPageController(CursoService service) {
        this.service = service;
    }

    // LISTA DE CURSOS
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", service.listar());
        return "cursos/lista"; // templates/cursos/lista.html
    }

    // FORMULÁRIO NOVO CURSO
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("curso", new Curso());
        return "cursos/form";  // templates/cursos/form.html
    }

    // SALVAR CURSO
    @PostMapping
    public String salvar(@ModelAttribute("curso") Curso curso) {
        service.criar(curso);
        return "redirect:/cursos";
    }
}
