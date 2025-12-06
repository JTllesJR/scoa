package br.com.scoa.service;

import br.com.scoa.model.Aluno;
import br.com.scoa.model.Matricula;
import br.com.scoa.model.Turma;
import br.com.scoa.repository.AlunoRepository;
import br.com.scoa.repository.MatriculaRepository;
import br.com.scoa.repository.TurmaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository repo;
    private final AlunoRepository alunoRepo;
    private final TurmaRepository turmaRepo;

    public MatriculaService(MatriculaRepository repo,
                            AlunoRepository alunoRepo,
                            TurmaRepository turmaRepo) {
        this.repo = repo;
        this.alunoRepo = alunoRepo;
        this.turmaRepo = turmaRepo;
    }

    /**
     * Regra principal de matrícula:
     * - Verifica se aluno e turma existem;
     * - Impede matrícula duplicada na mesma turma;
     * - Verifica capacidade da turma (se informada);
     * - Cria e salva a matrícula.
     */
    public Matricula matricular(Long alunoId, Long turmaId, String semestre) {

        // 1) Buscar aluno e turma
        Aluno aluno = alunoRepo.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        Turma turma = turmaRepo.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

        // 2) Impedir matrícula duplicada (mesmo aluno, mesma turma)
        if (repo.existsByAlunoIdAndTurmaId(alunoId, turmaId)) {
            throw new RuntimeException("Aluno já está matriculado nesta turma.");
        }

        // 3) Verificar capacidade da turma (se tiver capacidade definida)
        Integer capacidade = turma.getCapacidade(); // pode ser null
        if (capacidade != null) {
            int matriculados = repo.countByTurmaId(turma.getId());
            if (matriculados >= capacidade) {
                throw new RuntimeException("Turma sem vagas disponíveis.");
            }
        }

        // 4) Criar a matrícula
        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setTurma(turma);

        // se o semestre vier vazio, usa o semestre da turma
        if (semestre != null && !semestre.isBlank()) {
            matricula.setSemestre(semestre);
        } else {
            matricula.setSemestre(turma.getSemestre());
        }

        // 5) Salvar
        return repo.save(matricula);
    }

    // Versão antiga, com 2 parâmetros, delega para a nova
    public Matricula matricular(Long alunoId, Long turmaId) {
        Turma turma = turmaRepo.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
        return matricular(alunoId, turmaId, turma.getSemestre());
    }

    public List<Matricula> listar() {
        return repo.findAll();
    }
}
