package br.com.scoa.repository;

import br.com.scoa.model.Obra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObraRepository extends JpaRepository<Obra, Long> {

    boolean existsByCodigoAcervo(String codigoAcervo);
}
