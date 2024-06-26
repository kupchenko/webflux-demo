package me.kupchenko.webfluxdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kupchenko.webfluxdemo.controller.UpdateOrderRequest;
import me.kupchenko.webfluxdemo.dto.AddressDto;
import me.kupchenko.webfluxdemo.dto.OrderDto;
import me.kupchenko.webfluxdemo.dto.request.CreateOrderRequest;
import me.kupchenko.webfluxdemo.dto.request.SearchOrdersRequest;
import me.kupchenko.webfluxdemo.dto.response.OrdersResponse;
import me.kupchenko.webfluxdemo.exception.InvalidRequestException;
import me.kupchenko.webfluxdemo.model.Address;
import me.kupchenko.webfluxdemo.model.Client;
import me.kupchenko.webfluxdemo.model.Order;
import me.kupchenko.webfluxdemo.repository.AddressRepository;
import me.kupchenko.webfluxdemo.repository.ClientRepository;
import me.kupchenko.webfluxdemo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
  private final AddressRepository addressRepository;
  private final OrderRepository orderRepository;
  private final ClientRepository clientRepository;

  @Value("${default.pilotes.price}")
  private BigDecimal price;
  @Value("${default.pilotes.amounts}")
  private List<Integer> pilotesAmounts;
  @Value("${default.pilotes.orderModificationDuration}")
  private Integer orderModificationDuration;

  public Mono<Order> createOrder(CreateOrderRequest request) {
    if (!isValidRequest(request)) {
      log.info("Request is invalid: {}", request);
      return Mono.error(InvalidRequestException::new);
    }
    return Mono.just(request)
        .switchIfEmpty(Mono.error(IllegalStateException::new))
        .flatMap(req -> Mono.zip(
            addressRepository.save(toAddress(req.getAddress())),
            clientRepository.findById(req.getClientId())
        ))
        .map(tuple -> mapToOrder(request, tuple.getT1(), tuple.getT2()))
        .flatMap(orderRepository::save);
  }

  private Address toAddress(AddressDto addressDto) {
    Address address = new Address();
    address.setCity(addressDto.getCity());
    address.setStreet(addressDto.getStreet());
    address.setCountry(addressDto.getCountry());
    address.setPostcode(addressDto.getPostcode());
    return address;
  }

  private boolean isValidRequest(CreateOrderRequest request) {
    if (Objects.isNull(request) || Objects.isNull(request.getAddress())) {
      return false;
    }
    return pilotesAmounts.contains(request.getNumberOfPilots());
  }

  private Order mapToOrder(CreateOrderRequest request, Address address, Client client) {
    Order order = new Order();
    order.setPilotes(request.getNumberOfPilots());
    order.setOrderTotal(price.multiply(BigDecimal.valueOf(request.getNumberOfPilots())));
    order.setClientFk(client.getId());
    order.setAddressFk(address.getId());
    order.setCreatedAt(LocalDateTime.now());
    return order;
  }

  public Mono<OrdersResponse> fetchAllOrders(SearchOrdersRequest request) {
    return clientRepository.findByFirstNameStartsWithAndLastNameStartsWith(request.getUserNamePrefix(), request.getUserNamePrefix())
        .collectList()
        .map(this::toClientIds)
        .flatMapMany(orderRepository::findAllByClientFkIn)
        .flatMap(this::populateAddress)
        .flatMap(this::populateClient)
        .map(this::mapToOrderDto)
        .collectList()
        .map(OrdersResponse::new);
  }

  private List<Long> toClientIds(List<Client> clients) {
    return clients.stream().map(Client::getId).collect(Collectors.toList());
  }

  private Mono<Order> populateClient(Order order) {
    return Mono.just(order)
        .flatMap(o -> {
          if (o.getClientFk() == null) {
            return Mono.just(o);
          }
          return clientRepository.findById(o.getClientFk()).map(client -> {
            o.setClient(client);
            return o;
          });
        });
  }

  private Mono<Order> populateAddress(Order order) {
    return Mono.just(order)
        .flatMap(o -> {
          if (o.getAddressFk() == null) {
            return Mono.just(o);
          }
          return addressRepository.findById(o.getAddressFk()).map(address -> {
            o.setDeliveryAddress(address);
            return o;
          });
        });
  }

  private OrderDto mapToOrderDto(Order order) {
    OrderDto orderDto = new OrderDto();
    orderDto.setId(order.getId());
    orderDto.setDeliveryAddress(Optional.ofNullable(order.getDeliveryAddress()).map(Address::toString).orElse("Unknown"));
    orderDto.setClient(Optional.ofNullable(order.getClient()).map(Client::toString).orElse("Unknown"));
    orderDto.setPilotes(order.getPilotes());
    orderDto.setOrderTotal(order.getOrderTotal());
    return orderDto;
  }

  public Mono<Order> updateOrder(Long orderId, UpdateOrderRequest request) {
    LocalDateTime now = LocalDateTime.now();
    return orderRepository.findById(orderId)
        .flatMap(order -> checkIfOrderExpiredForUpdate(now, order))
        .map(order -> updateNumberOfPilotes(request, order))
        .flatMap(order -> updateAddress(order, request))
        .flatMap(orderRepository::save);
  }

  private Mono<Order> checkIfOrderExpiredForUpdate(LocalDateTime now, Order order) {
    long minutes = ChronoUnit.MINUTES.between(now, order.getCreatedAt());
    if (minutes > orderModificationDuration) {
      return Mono.error(IllegalStateException::new);
    }
    return Mono.just(order);
  }

  private Mono<Order> updateAddress(Order order, UpdateOrderRequest request) {
    if (Objects.isNull(request.getAddress())) {
      return Mono.just(order);
    }
    return addressRepository.save(toAddress(request.getAddress()))
        .map(address -> {
          order.setAddressFk(address.getId());
          return order;
        });
  }

  private Order updateNumberOfPilotes(UpdateOrderRequest request, Order order) {
    if (Objects.nonNull(request.getNumberOfPilotes())) {
      order.setPilotes(request.getNumberOfPilotes());
      order.setOrderTotal(price.multiply(BigDecimal.valueOf(request.getNumberOfPilotes())));
    }
    return order;
  }
}
