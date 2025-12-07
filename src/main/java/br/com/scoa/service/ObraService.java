package br.com.scoa.service;

import br.com.scoa.model.Obra;
import br.com.scoa.repository.ObraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObraService {

    private final ObraRepository repo;

    public ObraService(ObraRepository repo) {
        this.repo = repo;
    }

    public List<Obra> listarTodas() {
        return repo.findAll();
    }

    public Obra buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada."));
    }

    public Obra salvar(Obra obra) {

        if (obra.getId() == null) {
            // nova obra, garante código único
            if (repo.existsByCodigoAcervo(obra.getCodigoAcervo())) {
                throw new RuntimeException("Já existe uma obra com esse código de acervo.");
            }
            // começa disponível
            obra.setDisponivel(true);
        }

        return repo.save(obra);
    }
}
