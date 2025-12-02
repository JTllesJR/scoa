package br.com.scoa.controller;

import br.com.scoa.model.Aluno;
import br.com.scoa.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/alunos")
public class AlunoPageController {

    private final AlunoService alunoService;

    public AlunoPageController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        return "alunos/lista"; // <-- nome do template
    }

    @GetMapping("/novo")
    public String mostrarFormularioNovoAluno(Model model) {
        model.addAttribute("aluno", new Aluno());
        return "alunos/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute("aluno") Aluno aluno) {
        alunoService.criarAluno(aluno);
        return "redirect:/alunos";
    }
}
