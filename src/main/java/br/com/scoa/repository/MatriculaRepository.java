package br.com.scoa.repository;

import br.com.scoa.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    // Regra de matrícula (já usada)
    boolean existsByAlunoIdAndTurmaId(Long alunoId, Long turmaId);

    int countByTurmaId(Long turmaId);

    // Choque de horário no semestre
    List<Matricula> findByAlunoIdAndSemestre(Long alunoId, String semestre);

    // Lançamento de notas por turma
    List<Matricula> findByTurmaId(Long turmaId);

    // Histórico do aluno (para boletim e pré-requisitos)
    List<Matricula> findByAlunoIdOrderBySemestreAsc(Long alunoId);
}
