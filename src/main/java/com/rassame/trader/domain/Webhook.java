package com.rassame.trader.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@Accessors(chain = true)
public class Webhook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tradeId;
}
