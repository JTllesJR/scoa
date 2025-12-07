package br.com.scoa.repository;

import br.com.scoa.model.ItemAlmoxarifado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAlmoxarifadoRepository extends JpaRepository<ItemAlmoxarifado, Long> {

    boolean existsByCodigo(String codigo);
}
