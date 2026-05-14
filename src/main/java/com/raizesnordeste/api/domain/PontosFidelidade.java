package com.raizesnordeste.api.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pontos_fidelidade")
public class PontosFidelidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @Column(nullable = false)
    private Integer pontos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    private String descricao;

    private LocalDateTime dataMovimentacao;

    @PrePersist
    public void prePersist() {
        this.dataMovimentacao = LocalDateTime.now();
    }

    public enum TipoMovimentacao {
        CREDITO, DEBITO
    }
}