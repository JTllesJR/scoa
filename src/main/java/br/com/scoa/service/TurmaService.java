package br.com.scoa.service;

import br.com.scoa.model.Disciplina;
import br.com.scoa.model.Professor;
import br.com.scoa.model.Turma;
import br.com.scoa.repository.DisciplinaRepository;
import br.com.scoa.repository.ProfessorRepository;
import br.com.scoa.repository.TurmaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;

    public TurmaService(TurmaRepository turmaRepository,
                        DisciplinaRepository disciplinaRepository,
                        ProfessorRepository professorRepository) {
        this.turmaRepository = turmaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
    }

    public Turma criar(Long idDisciplina, Long idProfessor, Turma turma) {
        Disciplina disciplina = disciplinaRepository.findById(idDisciplina)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        Professor professor = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        turma.setDisciplina(disciplina);
        turma.setProfessor(professor);

        return turmaRepository.save(turma);
    }

    public List<Turma> listar() {
        return turmaRepository.findAll();
    }

    public Turma buscar(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
    }

    public void deletar(Long id) {
        turmaRepository.deleteById(id);
    }
}
