package me.kupchenko.webfluxdemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table(name = "orders")
public class Order {
  @Id
  private Long id;
  private Long addressFk;
  @Transient
  private Address deliveryAddress;
  private Long clientFk;
  @Transient
  private Client client;
  private int pilotes;
  private BigDecimal orderTotal;
  private LocalDateTime createdAt;
}
