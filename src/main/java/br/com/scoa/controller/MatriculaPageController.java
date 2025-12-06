package br.com.scoa.controller;

import br.com.scoa.model.Matricula;
import br.com.scoa.model.Turma;
import br.com.scoa.service.AlunoService;
import br.com.scoa.service.MatriculaService;
import br.com.scoa.service.TurmaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // LISTAGEM GERAL: /matriculas
    @GetMapping
    public String listar(Model model) {
        List<Matricula> matriculas = matriculaService.listar();
        model.addAttribute("matriculas", matriculas);
        return "matriculas/lista";
    }

    // FORM DE NOVA MATRÍCULA: /matriculas/novo
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("turmas", turmaService.listar());
        return "matriculas/form";
    }

    // SALVAR MATRÍCULA (já com regras de negócio)
    @PostMapping
    public String salvar(@RequestParam("alunoId") Long alunoId,
                         @RequestParam("turmaId") Long turmaId,
                         @RequestParam("semestre") String semestre,
                         Model model) {

        try {
            matriculaService.matricular(alunoId, turmaId, semestre);
            return "redirect:/matriculas";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("alunos", alunoService.listarTodos());
            model.addAttribute("turmas", turmaService.listar());
            return "matriculas/form";
        }
    }

    // ================== MÓDULO NOTAS/FREQUÊNCIA ==================

    // TELA DE LANÇAMENTO: /matriculas/turma/{id}/lancamentos (GET)
    @GetMapping("/turma/{turmaId}/lancamentos")
    public String telaLancamentos(@PathVariable Long turmaId, Model model,
                                  @ModelAttribute("mensagem") String mensagem) {

        Turma turma = turmaService.buscar(turmaId);
        List<Matricula> matriculas = matriculaService.listarPorTurma(turmaId);

        model.addAttribute("turma", turma);
        model.addAttribute("matriculas", matriculas);

        if (mensagem != null && !mensagem.isBlank()) {
            model.addAttribute("mensagem", mensagem);
        }

        return "notas/lancamento";
    }

    // SALVAR LANÇAMENTOS: /matriculas/turma/{id}/lancamentos (POST)
    @PostMapping("/turma/{turmaId}/lancamentos")
    public String salvarLancamentos(@PathVariable Long turmaId,
                                    @RequestParam("matriculaId") Long[] matriculaIds,
                                    @RequestParam("nota") Double[] notas,
                                    @RequestParam("faltas") Integer[] faltas,
                                    RedirectAttributes redirectAttributes) {

        for (int i = 0; i < matriculaIds.length; i++) {
            Long idMatricula = matriculaIds[i];
            Double nota = (notas != null && notas.length > i) ? notas[i] : null;
            Integer falta = (faltas != null && faltas.length > i) ? faltas[i] : null;

            matriculaService.atualizarNotaEFaltas(idMatricula, nota, falta);
        }

        redirectAttributes.addFlashAttribute("mensagem", "Lançamentos salvos com sucesso.");
        return "redirect:/matriculas/turma/" + turmaId + "/lancamentos";
    }
}
