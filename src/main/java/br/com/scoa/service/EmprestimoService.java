package br.com.scoa.service;

import br.com.scoa.model.Aluno;
import br.com.scoa.model.Emprestimo;
import br.com.scoa.model.Obra;
import br.com.scoa.repository.AlunoRepository;
import br.com.scoa.repository.EmprestimoRepository;
import br.com.scoa.repository.ObraRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository repo;
    private final AlunoRepository alunoRepo;
    private final ObraRepository obraRepo;

    public EmprestimoService(EmprestimoRepository repo,
                             AlunoRepository alunoRepo,
                             ObraRepository obraRepo) {
        this.repo = repo;
        this.alunoRepo = alunoRepo;
        this.obraRepo = obraRepo;
    }

    public List<Emprestimo> listarTodos() {
        return repo.findAll();
    }

    public List<Emprestimo> listarPorAluno(Long alunoId) {
        return repo.findByAlunoId(alunoId);
    }

    public Emprestimo buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado."));
    }

    public Emprestimo registrarEmprestimo(Long alunoId,
                                          Long obraId,
                                          LocalDate dataPrevistaDevolucao) {

        Aluno aluno = alunoRepo.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        Obra obra = obraRepo.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada."));

        if (obra.getDisponivel() == null || !obra.getDisponivel()) {
            throw new RuntimeException("Obra não está disponível para empréstimo.");
        }

        Emprestimo e = new Emprestimo();
        e.setAluno(aluno);
        e.setObra(obra);
        e.setDataEmprestimo(LocalDate.now());

        if (dataPrevistaDevolucao == null) {
            // prazo padrão: 7 dias
            e.setDataPrevistaDevolucao(LocalDate.now().plusDays(7));
        } else {
            e.setDataPrevistaDevolucao(dataPrevistaDevolucao);
        }

        e.setDevolvido(false);

        // marca obra como não disponível
        obra.setDisponivel(false);
        obraRepo.save(obra);

        return repo.save(e);
    }

    public void registrarDevolucao(Long emprestimoId) {
        Emprestimo e = buscarPorId(emprestimoId);

        if (e.getDevolvido() != null && e.getDevolvido()) {
            return;
        }

        e.setDevolvido(true);
        e.setDataDevolucao(LocalDate.now());

        // devolve a obra
        Obra obra = e.getObra();
        if (obra != null) {
            obra.setDisponivel(true);
            obraRepo.save(obra);
        }

        repo.save(e);
    }
}
