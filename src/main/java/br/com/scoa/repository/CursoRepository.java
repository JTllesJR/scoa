package br.com.scoa.repository;

import br.com.scoa.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    // opcional: métodos específicos depois, ex: findByNome(String nome)
}
