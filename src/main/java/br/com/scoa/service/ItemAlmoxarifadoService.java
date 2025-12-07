package br.com.scoa.service;

import br.com.scoa.model.ItemAlmoxarifado;
import br.com.scoa.repository.ItemAlmoxarifadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemAlmoxarifadoService {

    private final ItemAlmoxarifadoRepository repo;

    public ItemAlmoxarifadoService(ItemAlmoxarifadoRepository repo) {
        this.repo = repo;
    }

    public List<ItemAlmoxarifado> listarTodos() {
        return repo.findAll();
    }

    public ItemAlmoxarifado buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de almoxarifado não encontrado."));
    }

    public ItemAlmoxarifado salvar(ItemAlmoxarifado item) {

        if (item.getId() == null) {
            // novo item
            if (item.getCodigo() != null && !item.getCodigo().isBlank()
                    && repo.existsByCodigo(item.getCodigo())) {
                throw new RuntimeException("Já existe um item com este código.");
            }
            if (item.getQuantidadeAtual() == null) {
                item.setQuantidadeAtual(0);
            }
        }

        return repo.save(item);
    }

    public void atualizarQuantidade(ItemAlmoxarifado item, int novaQuantidade) {
        item.setQuantidadeAtual(novaQuantidade);
        repo.save(item);
    }
}
