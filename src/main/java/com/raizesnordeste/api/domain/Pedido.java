package com.raizesnordeste.api.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalPedido canalPedido;

    @Column(nullable = false)
    private BigDecimal total;

    private LocalDateTime dataPedido;

    @PrePersist
    public void prePersist() {
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO;
    }

    public enum StatusPedido {
        AGUARDANDO_PAGAMENTO, EM_PREPARO, PRONTO, ENTREGUE, CANCELADO
    }

    public enum CanalPedido {
        APP, TOTEM, BALCAO, PICKUP, WEB
    }
}