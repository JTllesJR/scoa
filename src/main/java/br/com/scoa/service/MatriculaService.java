package br.com.scoa.service;

import br.com.scoa.model.Aluno;
import br.com.scoa.model.Matricula;
import br.com.scoa.model.Turma;
import br.com.scoa.repository.AlunoRepository;
import br.com.scoa.repository.MatriculaRepository;
import br.com.scoa.repository.TurmaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
     * MATRÍCULA PRINCIPAL (com semestre explícito)
     */
    public Matricula matricular(Long alunoId, Long turmaId, String semestreInformado) {

        // 1) Buscar aluno e turma
        Aluno aluno = alunoRepo.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        Turma turma = turmaRepo.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

        // Semestre efetivo
        String semestre = (semestreInformado != null && !semestreInformado.isBlank())
                ? semestreInformado
                : turma.getSemestre();

        // 2) Matrícula duplicada
        if (repo.existsByAlunoIdAndTurmaId(alunoId, turmaId)) {
            throw new RuntimeException("Aluno já está matriculado nesta turma.");
        }

        // 3) Capacidade da turma
        Integer capacidade = turma.getCapacidade();
        if (capacidade != null) {
            int matriculados = repo.countByTurmaId(turma.getId());
            if (matriculados >= capacidade) {
                throw new RuntimeException("Turma sem vagas disponíveis.");
            }
        }

        // 4) Choque de horário
        verificarChoqueHorario(alunoId, turma, semestre);

        // 5) Criar matrícula
        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setTurma(turma);
        matricula.setSemestre(semestre);

        // nota e faltas começam null
        matricula.setNota(null);
        matricula.setFaltas(null);

        return repo.save(matricula);
    }

    // Versão com 2 parâmetros, usando semestre da turma
    public Matricula matricular(Long alunoId, Long turmaId) {
        Turma turma = turmaRepo.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
        return matricular(alunoId, turmaId, turma.getSemestre());
    }

    public List<Matricula> listar() {
        return repo.findAll();
    }

    // NOVO: listar matrículas de uma turma (para lançar notas/frequência)
    public List<Matricula> listarPorTurma(Long turmaId) {
        return repo.findByTurmaId(turmaId);
    }

    // NOVO: atualizar nota e faltas de uma matrícula
    public void atualizarNotaEFaltas(Long matriculaId, Double nota, Integer faltas) {
        Matricula m = repo.findById(matriculaId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada."));

        m.setNota(nota);
        m.setFaltas(faltas);

        repo.save(m);
    }

    // listar matrículas de um aluno (para boletim)
    public List<Matricula> listarPorAluno(Long alunoId) {
        return repo.findByAlunoIdOrderBySemestreAsc(alunoId);
    }

    /**
     * Verifica choque de horário com outras turmas do mesmo semestre
     */
    private void verificarChoqueHorario(Long alunoId, Turma novaTurma, String semestre) {

        if (novaTurma.getDiaSemana() == null ||
                novaTurma.getHoraInicio() == null ||
                novaTurma.getHoraFim() == null) {
            return;
        }

        List<Matricula> matriculasAluno = repo.findByAlunoIdAndSemestre(alunoId, semestre);

        for (Matricula m : matriculasAluno) {
            Turma t = m.getTurma();

            if (t == null ||
                    t.getDiaSemana() == null ||
                    t.getHoraInicio() == null ||
                    t.getHoraFim() == null) {
                continue;
            }

            // mesmo dia?
            if (!t.getDiaSemana().equalsIgnoreCase(novaTurma.getDiaSemana())) {
                continue;
            }

            // horários sobrepostos?
            if (horarioSobreposto(
                    t.getHoraInicio(), t.getHoraFim(),
                    novaTurma.getHoraInicio(), novaTurma.getHoraFim())) {

                String msg = String.format(
                        "Choque de horário com a turma %d (%s %s-%s).",
                        t.getId(),
                        t.getDiaSemana(),
                        t.getHoraInicio(),
                        t.getHoraFim()
                );
                throw new RuntimeException(msg);
            }
        }
    }

    private boolean horarioSobreposto(String ini1, String fim1,
                                      String ini2, String fim2) {

        LocalTime inicio1 = LocalTime.parse(ini1);
        LocalTime fim1T = LocalTime.parse(fim1);
        LocalTime inicio2 = LocalTime.parse(ini2);
        LocalTime fim2T = LocalTime.parse(fim2);

        return inicio1.isBefore(fim2T) && inicio2.isBefore(fim1T);
    }
}
