package br.com.scoa.repository;

import br.com.scoa.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    // Verifica se já existe matrícula do mesmo aluno na mesma turma
    boolean existsByAlunoIdAndTurmaId(Long alunoId, Long turmaId);

    // Conta quantos alunos estão matriculados em uma turma
    int countByTurmaId(Long turmaId);
}
