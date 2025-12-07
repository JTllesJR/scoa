package br.com.scoa.model;

import jakarta.persistence.*;

@Entity
@Table(name = "obras")
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column
    private String autor;

    @Column(unique = true, nullable = false)
    private String codigoAcervo; // tipo código de barras / tombo

    @Column
    private Boolean disponivel;

    public Obra() {
    }

    // ========== GETTERS / SETTERS ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCodigoAcervo() {
        return codigoAcervo;
    }

    public void setCodigoAcervo(String codigoAcervo) {
        this.codigoAcervo = codigoAcervo;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}
