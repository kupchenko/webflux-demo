package me.kupchenko.webfluxdemo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDto {
  private Long id;
  private String deliveryAddress;
  private String client;
  private int pilotes;
  private BigDecimal orderTotal;
}
