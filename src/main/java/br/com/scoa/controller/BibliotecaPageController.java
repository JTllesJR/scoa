package br.com.scoa.controller;

import br.com.scoa.service.AlunoService;
import br.com.scoa.service.EmprestimoService;
import br.com.scoa.service.ObraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/biblioteca")
public class BibliotecaPageController {

    private final EmprestimoService emprestimoService;
    private final AlunoService alunoService;
    private final ObraService obraService;

    public BibliotecaPageController(EmprestimoService emprestimoService,
                                    AlunoService alunoService,
                                    ObraService obraService) {
        this.emprestimoService = emprestimoService;
        this.alunoService = alunoService;
        this.obraService = obraService;
    }

    // LISTAGEM GERAL: /biblioteca
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        model.addAttribute("tituloPagina", "Empréstimos - Biblioteca");
        model.addAttribute("aluno", null);
        return "biblioteca/emprestimos";
    }

    // LISTAGEM POR ALUNO: /biblioteca/aluno/{id}
    @GetMapping("/aluno/{alunoId}")
    public String listarPorAluno(@PathVariable Long alunoId, Model model) {
        var aluno = alunoService.buscarPorId(alunoId);

        model.addAttribute("emprestimos", emprestimoService.listarPorAluno(alunoId));
        model.addAttribute("tituloPagina", "Empréstimos do Aluno");
        model.addAttribute("aluno", aluno);

        return "biblioteca/emprestimos";
    }

    // FORM DE EMPRÉSTIMO: /biblioteca/emprestar/{obraId}
    @GetMapping("/emprestar/{obraId}")
    public String emprestarForm(@PathVariable Long obraId, Model model) {

        var obra = obraService.buscarPorId(obraId);

        model.addAttribute("obra", obra);
        model.addAttribute("alunos", alunoService.listarTodos());

        return "biblioteca/emprestar";
    }

    // SALVAR EMPRÉSTIMO: POST /biblioteca/emprestar
    @PostMapping("/emprestar")
    public String emprestar(@RequestParam("alunoId") Long alunoId,
                            @RequestParam("obraId") Long obraId,
                            @RequestParam(value = "dataPrevistaDevolucao", required = false)
                            String dataPrevistaDevolucaoStr,
                            Model model) {

        try {
            LocalDate dataPrevista = null;
            if (dataPrevistaDevolucaoStr != null && !dataPrevistaDevolucaoStr.isBlank()) {
                dataPrevista = LocalDate.parse(dataPrevistaDevolucaoStr);
            }

            emprestimoService.registrarEmprestimo(alunoId, obraId, dataPrevista);
            return "redirect:/biblioteca";

        } catch (RuntimeException e) {
            // em caso de erro, volta pro form com mensagem
            var obra = obraService.buscarPorId(obraId);
            model.addAttribute("obra", obra);
            model.addAttribute("alunos", alunoService.listarTodos());
            model.addAttribute("erro", e.getMessage());
            return "biblioteca/emprestar";
        }
    }

    // DEVOLUÇÃO: POST /biblioteca/{id}/devolver
    @PostMapping("/{id}/devolver")
    public String devolver(@PathVariable Long id,
                           @RequestParam(value = "redirectAlunoId", required = false)
                           Long redirectAlunoId) {

        emprestimoService.registrarDevolucao(id);

        if (redirectAlunoId != null) {
            return "redirect:/biblioteca/aluno/" + redirectAlunoId;
        }

        return "redirect:/biblioteca";
    }
}
