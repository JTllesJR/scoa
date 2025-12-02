package br.com.scoa.service;

import br.com.scoa.model.Aluno;
import br.com.scoa.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    // injeção via construtor
    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno criarAluno(Aluno aluno) {

        alunoRepository.findByCpf(aluno.getCpf()).ifPresent(a -> {
            throw new RuntimeException("Já existe um aluno com esse CPF");
        });

        if (aluno.getSituacaoAcademica() == null) {
            aluno.setSituacaoAcademica("ATIVO");
        }

        aluno.setPendenciaFinanceira(false);
        aluno.setPendenciaBiblioteca(false);

        return alunoRepository.save(aluno);
    }

    // 🔹 Método antigo, usado pelo AlunoController (REST)
    public List<Aluno> listar() {
        return alunoRepository.findAll();
    }

    // 🔹 Método que o AlunoPageController está usando
    public List<Aluno> listarTodos() {
        return listar(); // reaproveita o outro
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    public void deletar(Long id) {
        alunoRepository.deleteById(id);
    }
}
