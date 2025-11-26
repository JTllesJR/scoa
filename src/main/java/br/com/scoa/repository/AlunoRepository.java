package br.com.scoa.repository;

import br.com.scoa.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByCpf(String cpf);
}
