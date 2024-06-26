package me.kupchenko.webfluxdemo.dto.request;

import lombok.Data;
import me.kupchenko.webfluxdemo.dto.AddressDto;

@Data
public class CreateOrderRequest {
  private int numberOfPilots;
  private long clientId;
  private AddressDto address;
}
