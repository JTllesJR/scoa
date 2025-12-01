package br.com.scoa.service;

import br.com.scoa.model.Professor;
import br.com.scoa.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository repo;

    public ProfessorService(ProfessorRepository repo) {
        this.repo = repo;
    }

    public Professor criar(Professor p) {
        return repo.save(p);
    }

    public List<Professor> listar() {
        return repo.findAll();
    }

    public Professor buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
    }
}
