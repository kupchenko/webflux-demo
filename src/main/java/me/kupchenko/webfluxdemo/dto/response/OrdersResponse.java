package me.kupchenko.webfluxdemo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.kupchenko.webfluxdemo.dto.OrderDto;

import java.util.List;

@Data
@AllArgsConstructor
public class OrdersResponse {
  private List<OrderDto> orders;
}
