package com.rassame.trader.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Accessors(chain = true)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 9000)
    private String trade;
    @Column(length = 9000)
    private String transaction;
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    private LocalDateTime created;

    private Double sent;
    private Double received;
    private String currency;

    private String account;
}
