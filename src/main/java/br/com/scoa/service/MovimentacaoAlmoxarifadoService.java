package br.com.scoa.service;

import br.com.scoa.model.ItemAlmoxarifado;
import br.com.scoa.model.MovimentacaoAlmoxarifado;
import br.com.scoa.repository.MovimentacaoAlmoxarifadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimentacaoAlmoxarifadoService {

    private final MovimentacaoAlmoxarifadoRepository repo;
    private final ItemAlmoxarifadoService itemService;

    public MovimentacaoAlmoxarifadoService(MovimentacaoAlmoxarifadoRepository repo,
                                           ItemAlmoxarifadoService itemService) {
        this.repo = repo;
        this.itemService = itemService;
    }

    public List<MovimentacaoAlmoxarifado> listarTodas() {
        return repo.findAll();
    }

    public List<MovimentacaoAlmoxarifado> listarPorItem(Long itemId) {
        return repo.findByItemId(itemId);
    }

    public MovimentacaoAlmoxarifado registrarMovimentacao(Long itemId,
                                                          String tipo,
                                                          Integer quantidade,
                                                          String observacao) {

        if (quantidade == null || quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero.");
        }

        ItemAlmoxarifado item = itemService.buscarPorId(itemId);

        // estoque atual
        int atual = (item.getQuantidadeAtual() != null) ? item.getQuantidadeAtual() : 0;

        if ("ENTRADA".equalsIgnoreCase(tipo)) {
            atual += quantidade;
        } else if ("SAIDA".equalsIgnoreCase(tipo)) {
            if (quantidade > atual) {
                throw new RuntimeException("Estoque insuficiente para saída.");
            }
            atual -= quantidade;
        } else {
            throw new RuntimeException("Tipo de movimentação inválido.");
        }

        // atualiza estoque
        itemService.atualizarQuantidade(item, atual);

        // registra movimentação
        MovimentacaoAlmoxarifado mov = new MovimentacaoAlmoxarifado();
        mov.setItem(item);
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(tipo.toUpperCase());
        mov.setQuantidade(quantidade);
        mov.setObservacao(observacao);

        return repo.save(mov);
    }
}
