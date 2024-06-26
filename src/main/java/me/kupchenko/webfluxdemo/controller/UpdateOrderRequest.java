package me.kupchenko.webfluxdemo.controller;

import lombok.Data;
import me.kupchenko.webfluxdemo.dto.AddressDto;

@Data
public class UpdateOrderRequest {
  private Integer numberOfPilotes;
  private AddressDto address;
}
