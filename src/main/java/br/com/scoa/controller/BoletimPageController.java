package br.com.scoa.controller;

import br.com.scoa.model.Aluno;
import br.com.scoa.model.Matricula;
import br.com.scoa.model.Turma;
import br.com.scoa.service.AlunoService;
import br.com.scoa.service.MatriculaService;
import br.com.scoa.service.TurmaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/boletim")
public class BoletimPageController {

    private final AlunoService alunoService;
    private final MatriculaService matriculaService;
    private final TurmaService turmaService;

    public BoletimPageController(AlunoService alunoService,
                                 MatriculaService matriculaService,
                                 TurmaService turmaService) {
        this.alunoService = alunoService;
        this.matriculaService = matriculaService;
        this.turmaService = turmaService;
    }

    // ================== BOLETIM DO ALUNO ==================

    @GetMapping("/aluno/{alunoId}")
    public String boletimAluno(@PathVariable Long alunoId, Model model) {

        // no seu projeto o método se chama buscarPorId
        Aluno aluno = alunoService.buscarPorId(alunoId);
        List<Matricula> matriculas = matriculaService.listarPorAluno(alunoId);

        model.addAttribute("aluno", aluno);
        model.addAttribute("matriculas", matriculas);

        return "boletim/aluno";
    }

    // ================== BOLETIM DA TURMA ==================

    @GetMapping("/turma/{turmaId}")
    public String boletimTurma(@PathVariable Long turmaId, Model model) {

        Turma turma = turmaService.buscar(turmaId);
        List<Matricula> matriculas = matriculaService.listarPorTurma(turmaId);

        // Cálculo das estatísticas
        double somaNotas = 0.0;
        int qtdComNota = 0;
        int qtdAprovados = 0;
        int qtdReprovados = 0;
        int qtdEmAndamento = 0;

        for (Matricula m : matriculas) {
            Double nota = m.getNota();
            if (nota == null) {
                qtdEmAndamento++;
                continue;
            }

            somaNotas += nota;
            qtdComNota++;

            if (nota >= 6.0) {
                qtdAprovados++;
            } else {
                qtdReprovados++;
            }
        }

        Double mediaNotas = null;
        Double taxaAprovacao = null;

        if (qtdComNota > 0) {
            mediaNotas = somaNotas / qtdComNota;
            taxaAprovacao = (qtdAprovados * 100.0) / qtdComNota;
        }

        model.addAttribute("turma", turma);
        model.addAttribute("matriculas", matriculas);

        model.addAttribute("mediaNotas", mediaNotas);
        model.addAttribute("qtdAprovados", qtdAprovados);
        model.addAttribute("qtdReprovados", qtdReprovados);
        model.addAttribute("qtdEmAndamento", qtdEmAndamento);
        model.addAttribute("qtdComNota", qtdComNota);
        model.addAttribute("taxaAprovacao", taxaAprovacao);

        return "boletim/turma";
    }
}
