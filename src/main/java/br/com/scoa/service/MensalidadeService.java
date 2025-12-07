package br.com.scoa.service;

import br.com.scoa.model.Aluno;
import br.com.scoa.model.Mensalidade;
import br.com.scoa.repository.AlunoRepository;
import br.com.scoa.repository.MensalidadeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MensalidadeService {

    private final MensalidadeRepository repo;
    private final AlunoRepository alunoRepo;

    public MensalidadeService(MensalidadeRepository repo, AlunoRepository alunoRepo) {
        this.repo = repo;
        this.alunoRepo = alunoRepo;
    }

    public List<Mensalidade> listarTodas() {
        return repo.findAll();
    }

    public List<Mensalidade> listarPorAluno(Long alunoId) {
        return repo.findByAlunoId(alunoId);
    }

    public Mensalidade buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensalidade não encontrada."));
    }

    public Mensalidade salvar(Mensalidade mensalidade) {
        return repo.save(mensalidade);
    }

    /**
     * Cria uma nova mensalidade a partir de dados básicos.
     */
    public Mensalidade criarMensalidade(Long alunoId,
                                        String competencia,
                                        Double valor,
                                        LocalDate dataVencimento) {

        Aluno aluno = alunoRepo.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        Mensalidade m = new Mensalidade();
        m.setAluno(aluno);
        m.setCompetencia(competencia);
        m.setValor(valor);
        m.setDataVencimento(dataVencimento);
        m.setPago(false);
        m.setDataPagamento(null);

        return repo.save(m);
    }

    /**
     * Marca como paga (dataPagamento = hoje, pago = true).
     */
    public void marcarComoPaga(Long mensalidadeId) {
        Mensalidade m = buscarPorId(mensalidadeId);

        m.setPago(true);
        m.setDataPagamento(LocalDate.now());

        repo.save(m);
    }
}
