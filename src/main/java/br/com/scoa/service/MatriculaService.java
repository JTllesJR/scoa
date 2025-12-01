package br.com.scoa.service;

import br.com.scoa.model.Aluno;
import br.com.scoa.model.Turma;
import br.com.scoa.model.Matricula;
import br.com.scoa.repository.AlunoRepository;
import br.com.scoa.repository.MatriculaRepository;
import br.com.scoa.repository.TurmaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository,
                            AlunoRepository alunoRepository,
                            TurmaRepository turmaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    public Matricula matricular(Long alunoId, Long turmaId) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        // RN: bloqueio por pendências
        if (aluno.isPendenciaFinanceira() || aluno.isPendenciaBiblioteca()) {
            throw new RuntimeException("Aluno possui pendências e não pode se matricular.");
        }

        // RN: verificar capacidade (na versão simples)
        long qtdMatriculados = matriculaRepository.findAll().stream()
                .filter(m -> m.getTurma().getId().equals(turmaId))
                .count();

        if (qtdMatriculados >= turma.getCapacidade()) {
            throw new RuntimeException("Turma lotada.");
        }

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setTurma(turma);
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus("ATIVA");

        return matriculaRepository.save(matricula);
    }
}
