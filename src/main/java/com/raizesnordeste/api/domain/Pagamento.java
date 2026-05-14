package com.raizesnordeste.api.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaPagamento formaPagamento;

    private LocalDateTime dataPagamento;

    @PrePersist
    public void prePersist() {
        this.dataPagamento = LocalDateTime.now();
    }

    public enum StatusPagamento {
        PENDENTE, APROVADO, RECUSADO
    }

    public enum FormaPagamento {
        MOCK, PIX, CARTAO_CREDITO, CARTAO_DEBITO
    }
}