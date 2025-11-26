package br.com.scoa.service;

import br.com.scoa.model.Aluno;
import br.com.scoa.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno criarAluno(Aluno aluno) {
        alunoRepository.findByCpf(aluno.getCpf()).ifPresent(a -> {
            throw new RuntimeException("CPF já cadastrado.");
        });

        if (aluno.getSituacaoAcademica() == null) {
            aluno.setSituacaoAcademica("ATIVO");
        }

        aluno.setPendenciaFinanceira(false);
        aluno.setPendenciaBiblioteca(false);

        return alunoRepository.save(aluno);
    }

    public List<Aluno> listar() {
        return alunoRepository.findAll();
    }

    public Aluno buscar(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    public void deletar(Long id) {
        alunoRepository.deleteById(id);
    }
}
