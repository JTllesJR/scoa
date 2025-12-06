package br.com.scoa.service;

import br.com.scoa.model.Aluno;
import br.com.scoa.model.Disciplina;
import br.com.scoa.model.Matricula;
import br.com.scoa.model.Turma;
import br.com.scoa.repository.AlunoRepository;
import br.com.scoa.repository.MatriculaRepository;
import br.com.scoa.repository.TurmaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * MATRÍCULA PRINCIPAL (UC02 – Realizar matrícula/rematrícula)
     * Regras:
     * - Verifica existência de aluno e turma;
     * - Impede matrícula duplicada na mesma turma;
     * - Verifica capacidade da turma;
     * - Verifica pré-requisitos da disciplina (RN01);
     * - Verifica choque de horário;
     * - Cria e salva a matrícula.
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

        // 4) Verificar pré-requisitos (RN01)
        verificarPreRequisitos(alunoId, turma);

        // 5) Choque de horário
        verificarChoqueHorario(alunoId, turma, semestre);

        // 6) Criar matrícula
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

    // Lista matrículas de uma turma (para lançamento de notas)
    public List<Matricula> listarPorTurma(Long turmaId) {
        return repo.findByTurmaId(turmaId);
    }

    // Histórico do aluno (para boletim e RN01)
    public List<Matricula> listarPorAluno(Long alunoId) {
        return repo.findByAlunoIdOrderBySemestreAsc(alunoId);
    }

    // Atualizar nota e faltas (lançamento de notas/frequência)
    public void atualizarNotaEFaltas(Long matriculaId, Double nota, Integer faltas) {
        Matricula m = repo.findById(matriculaId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada."));

        m.setNota(nota);
        m.setFaltas(faltas);

        repo.save(m);
    }

    // ================== RN01 – Verificar pré-requisitos ==================

    /**
     * Verifica se o aluno já foi aprovado em todas as disciplinas
     * que são pré-requisito da disciplina desta turma.
     *
     * Regra de aprovação usada aqui: nota >= 6.0 (ajustável).
     */
    private void verificarPreRequisitos(Long alunoId, Turma novaTurma) {

        Disciplina disciplina = novaTurma.getDisciplina();
        if (disciplina == null) {
            return;
        }

        Set<Disciplina> preRequisitos = disciplina.getPreRequisitos();
        if (preRequisitos == null || preRequisitos.isEmpty()) {
            // disciplina sem pré-requisitos
            return;
        }

        // Histórico do aluno
        List<Matricula> historico = repo.findByAlunoIdOrderBySemestreAsc(alunoId);

        // Disciplinas em que o aluno foi aprovado (nota >= 6)
        Set<Long> disciplinasAprovadas = historico.stream()
                .filter(m -> m.getNota() != null
                        && m.getNota() >= 6.0
                        && m.getTurma() != null
                        && m.getTurma().getDisciplina() != null)
                .map(m -> m.getTurma().getDisciplina().getId())
                .collect(Collectors.toSet());

        // Verificar quais pré-requisitos ainda não foram cumpridos
        List<String> faltando = new ArrayList<>();
        for (Disciplina pre : preRequisitos) {
            if (pre.getId() == null) continue;
            if (!disciplinasAprovadas.contains(pre.getId())) {
                faltando.add(pre.getNome());
            }
        }

        if (!faltando.isEmpty()) {
            String msg = "Aluno não atende aos pré-requisitos desta disciplina. "
                    + "Pré-requisitos pendentes: " + String.join(", ", faltando) + ".";
            throw new RuntimeException(msg);
        }
    }

    // ================== Choque de horário (já implementado antes) ==================

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
