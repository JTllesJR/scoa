package br.com.scoa.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "alunos")   // nome da tabela no banco
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    private LocalDate dataNascimento;
    private String endereco;
    private String telefone;

    @Column(unique = true)
    private String email;

    private String situacaoAcademica;
    private boolean pendenciaFinanceira;
    private boolean pendenciaBiblioteca;

    public Aluno() {}


    // GETTERS E SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSituacaoAcademica() { return situacaoAcademica; }
    public void setSituacaoAcademica(String situacaoAcademica) { this.situacaoAcademica = situacaoAcademica; }

    public boolean isPendenciaFinanceira() { return pendenciaFinanceira; }
    public void setPendenciaFinanceira(boolean pendenciaFinanceira) { this.pendenciaFinanceira = pendenciaFinanceira; }

    public boolean isPendenciaBiblioteca() { return pendenciaBiblioteca; }
    public void setPendenciaBiblioteca(boolean pendenciaBiblioteca) { this.pendenciaBiblioteca = pendenciaBiblioteca; }
}
