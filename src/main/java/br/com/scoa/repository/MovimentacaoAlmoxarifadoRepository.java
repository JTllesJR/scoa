package br.com.scoa.repository;

import br.com.scoa.model.MovimentacaoAlmoxarifado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoAlmoxarifadoRepository extends JpaRepository<MovimentacaoAlmoxarifado, Long> {

    List<MovimentacaoAlmoxarifado> findByItemId(Long itemId);
}
