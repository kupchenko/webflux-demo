package me.kupchenko.webfluxdemo.controller;

import lombok.RequiredArgsConstructor;
import me.kupchenko.webfluxdemo.dto.request.CreateOrderRequest;
import me.kupchenko.webfluxdemo.dto.request.SearchOrdersRequest;
import me.kupchenko.webfluxdemo.dto.response.OrdersResponse;
import me.kupchenko.webfluxdemo.model.Order;
import me.kupchenko.webfluxdemo.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  public Mono<Order> createOrder(@RequestBody CreateOrderRequest request) {
    return orderService.createOrder(request);
  }

  @PatchMapping("{orderId}")
  public Mono<Order> updateOrder(@RequestBody UpdateOrderRequest request, @PathVariable Long orderId) {
    return orderService.updateOrder(orderId, request);
  }

  @GetMapping
  public Mono<OrdersResponse> fetchAllOrders(SearchOrdersRequest request) {
    return orderService.fetchAllOrders(request);
  }
}
