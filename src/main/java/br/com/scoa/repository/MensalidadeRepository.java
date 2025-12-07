package br.com.scoa.repository;

import br.com.scoa.model.Mensalidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long> {

    // listar mensalidades de um aluno específico
    List<Mensalidade> findByAlunoId(Long alunoId);
}
