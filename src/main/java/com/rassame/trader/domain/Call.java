package com.rassame.trader.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@Accessors(chain = true)
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tradeId;
    private String transferId;
    private String email;
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status;
    private String error;
}
