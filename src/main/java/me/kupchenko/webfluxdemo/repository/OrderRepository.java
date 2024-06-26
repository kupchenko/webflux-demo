package me.kupchenko.webfluxdemo.repository;

import me.kupchenko.webfluxdemo.model.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
  @Query("SELECT * from orders o WHERE o.client_fk IN (:clientIds)")
  Flux<Order> findAllByClientFkIn(List<Long> clientIds);
}
