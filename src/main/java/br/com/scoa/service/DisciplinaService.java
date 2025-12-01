package br.com.scoa.service;

import br.com.scoa.model.Curso;
import br.com.scoa.model.Disciplina;
import br.com.scoa.repository.CursoRepository;
import br.com.scoa.repository.DisciplinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository,
                             CursoRepository cursoRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
    }

    public Disciplina criar(Long idCurso, Disciplina disciplina) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        disciplina.setCurso(curso);

        return disciplinaRepository.save(disciplina);
    }

    public List<Disciplina> listar() {
        return disciplinaRepository.findAll();
    }

    public Disciplina buscar(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
    }

    public void deletar(Long id) {
        disciplinaRepository.deleteById(id);
    }
}
