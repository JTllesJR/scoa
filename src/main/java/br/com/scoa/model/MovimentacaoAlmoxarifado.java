package br.com.scoa.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "movimentacoes_almoxarifado")
public class MovimentacaoAlmoxarifado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemAlmoxarifado item;

    @Column(nullable = false)
    private LocalDate dataMovimentacao;

    // "ENTRADA" ou "SAIDA"
    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column
    private String observacao;

    public MovimentacaoAlmoxarifado() {
    }

    // ========== GETTERS / SETTERS ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemAlmoxarifado getItem() {
        return item;
    }

    public void setItem(ItemAlmoxarifado item) {
        this.item = item;
    }

    public LocalDate getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDate dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
