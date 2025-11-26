package br.com.scoa.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    private Integer cargaHorariaTotal; // em horas

    public Curso() {}

    public Curso(String nome, String descricao, Integer cargaHorariaTotal) {
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHorariaTotal = cargaHorariaTotal;
    }

    // getters e setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getCargaHorariaTotal() { return cargaHorariaTotal; }

    public void setCargaHorariaTotal(Integer cargaHorariaTotal) {
        this.cargaHorariaTotal = cargaHorariaTotal;
    }
}
