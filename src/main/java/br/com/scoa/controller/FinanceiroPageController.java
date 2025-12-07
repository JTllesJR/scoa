package br.com.scoa.controller;

import br.com.scoa.model.Mensalidade;
import br.com.scoa.service.AlunoService;
import br.com.scoa.service.MensalidadeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/financeiro")
public class FinanceiroPageController {

    private final MensalidadeService mensalidadeService;
    private final AlunoService alunoService;

    public FinanceiroPageController(MensalidadeService mensalidadeService,
                                    AlunoService alunoService) {
        this.mensalidadeService = mensalidadeService;
        this.alunoService = alunoService;
    }

    // LISTAGEM GERAL: /financeiro
    @GetMapping
    public String listarTodas(Model model) {
        List<Mensalidade> mensalidades = mensalidadeService.listarTodas();
        model.addAttribute("mensalidades", mensalidades);
        model.addAttribute("tituloPagina", "Mensalidades - Geral");
        model.addAttribute("aluno", null);
        return "financeiro/lista";
    }

    // LISTAGEM POR ALUNO: /financeiro/aluno/{id}
    @GetMapping("/aluno/{alunoId}")
    public String listarPorAluno(@PathVariable Long alunoId, Model model) {

        var aluno = alunoService.buscarPorId(alunoId);
        List<Mensalidade> mensalidades = mensalidadeService.listarPorAluno(alunoId);

        model.addAttribute("mensalidades", mensalidades);
        model.addAttribute("tituloPagina", "Mensalidades do Aluno");
        model.addAttribute("aluno", aluno);

        return "financeiro/lista";
    }

    // FORM NOVA MENSALIDADE: /financeiro/novo
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        return "financeiro/form";
    }

    // SALVAR NOVA MENSALIDADE: POST /financeiro
    @PostMapping
    public String salvar(@RequestParam("alunoId") Long alunoId,
                         @RequestParam("competencia") String competencia,
                         @RequestParam("valor") Double valor,
                         @RequestParam("dataVencimento") String dataVencimentoStr) {

        LocalDate dataVencimento = null;
        if (dataVencimentoStr != null && !dataVencimentoStr.isBlank()) {
            dataVencimento = LocalDate.parse(dataVencimentoStr);
        }

        mensalidadeService.criarMensalidade(alunoId, competencia, valor, dataVencimento);

        return "redirect:/financeiro";
    }

    // MARCAR COMO PAGA: POST /financeiro/{id}/pagar
    @PostMapping("/{id}/pagar")
    public String pagar(@PathVariable Long id,
                        @RequestParam(value = "redirectAlunoId", required = false) Long redirectAlunoId) {

        mensalidadeService.marcarComoPaga(id);

        if (redirectAlunoId != null) {
            return "redirect:/financeiro/aluno/" + redirectAlunoId;
        }

        return "redirect:/financeiro";
    }
}
