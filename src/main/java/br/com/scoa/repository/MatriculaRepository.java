package br.com.scoa.repository;

import br.com.scoa.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    boolean existsByAlunoIdAndTurmaId(Long alunoId, Long turmaId);

    int countByTurmaId(Long turmaId);

    List<Matricula> findByAlunoIdAndSemestre(Long alunoId, String semestre);

    List<Matricula> findByTurmaId(Long turmaId);

    // NOVO: todas as matrículas do aluno, ordenadas por semestre
    List<Matricula> findByAlunoIdOrderBySemestreAsc(Long alunoId);
}
