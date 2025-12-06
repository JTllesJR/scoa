package br.com.scoa.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "disciplinas")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // em horas
    private Integer cargaHoraria;

    // se for obrigatória no curso
    private Boolean obrigatoria;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    /**
     * Pré-requisitos desta disciplina (RN01 / RF01)
     * Ex.: "Estruturas de Dados" pode ter como pré "Programação I".
     */
    @ManyToMany
    @JoinTable(
            name = "disciplina_pre_requisito",
            joinColumns = @JoinColumn(name = "disciplina_id"),
            inverseJoinColumns = @JoinColumn(name = "pre_requisito_id")
    )
    private Set<Disciplina> preRequisitos = new HashSet<>();

    public Disciplina() {}

    // ========== GETTERS / SETTERS ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Boolean getObrigatoria() {
        return obrigatoria;
    }

    public void setObrigatoria(Boolean obrigatoria) {
        this.obrigatoria = obrigatoria;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Set<Disciplina> getPreRequisitos() {
        return preRequisitos;
    }

    public void setPreRequisitos(Set<Disciplina> preRequisitos) {
        this.preRequisitos = preRequisitos;
    }
}
