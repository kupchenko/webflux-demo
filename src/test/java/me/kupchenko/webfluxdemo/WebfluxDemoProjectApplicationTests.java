package me.kupchenko.webfluxdemo;

import me.kupchenko.webfluxdemo.dto.AddressDto;
import me.kupchenko.webfluxdemo.dto.request.CreateClientRequest;
import me.kupchenko.webfluxdemo.dto.request.CreateOrderRequest;
import me.kupchenko.webfluxdemo.model.Client;
import me.kupchenko.webfluxdemo.model.Order;
import me.kupchenko.webfluxdemo.repository.ClientRepository;
import me.kupchenko.webfluxdemo.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class WebfluxDemoProjectApplicationTests {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private ClientRepository clientRepository;

  @AfterEach
  public void cleanUp() {
    orderRepository.deleteAll().block();
    clientRepository.deleteAll().block();
  }

  @Test
  public void shouldCreateClientOrder() {
    // Given
    Client client = new Client();
    client.setFirstName("John");
    client.setLastName("Doe");
    Client saveClient = clientRepository.save(client).block();
    AddressDto addressDto = new AddressDto();
    addressDto.setCity("city");
    addressDto.setStreet("street");
    CreateOrderRequest createOrderRequest = new CreateOrderRequest();
    createOrderRequest.setClientId(saveClient.getId());
    createOrderRequest.setNumberOfPilots(5);
    createOrderRequest.setAddress(addressDto);
    // When
    webTestClient.post()
        .uri("/orders").contentType(MediaType.APPLICATION_JSON)
        .bodyValue(createOrderRequest)
        .exchange()
        .expectStatus().isOk();

    // Then
    List<Order> orders = orderRepository.findAll().collectList().block();
    assertThat(orders).hasSize(1);
    Order actualOrder = orders.get(0);
    assertThat(actualOrder.getOrderTotal()).isEqualTo(new BigDecimal("6.65"));
    assertThat(actualOrder.getAddressFk()).isEqualTo(1);
    assertThat(actualOrder.getClientFk()).isEqualTo(client.getId());
  }

  @Test
  public void shouldCreateClient() {
    // Given
    CreateClientRequest createClientRequest = new CreateClientRequest();
    createClientRequest.setFirstName("John");
    createClientRequest.setLastName("Doe");

    // When
    webTestClient.post()
        .uri("/clients").contentType(MediaType.APPLICATION_JSON)
        .bodyValue(createClientRequest)
        .exchange()
        .expectStatus().isOk();

    // Then
    List<Client> clients = clientRepository.findAll().collectList().block();
    assertThat(clients).hasSize(1);
    Client actualClient = clients.get(0);
    assertThat(actualClient.getFirstName()).isEqualTo(createClientRequest.getFirstName());
    assertThat(actualClient.getLastName()).isEqualTo(createClientRequest.getLastName());
  }

  @Test
  public void createClientOrder_shouldFailWhenAddressIsMissing() {
    // Given
    CreateOrderRequest createOrderRequest = new CreateOrderRequest();
    createOrderRequest.setNumberOfPilots(5);
    // When
    webTestClient.post()
        .uri("/orders").contentType(MediaType.APPLICATION_JSON)
        .bodyValue(createOrderRequest)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  public void createClientOrder_shouldFailWhenAmountIsNotFromConfigs() {
    // Given
    Client client = new Client();
    client.setFirstName("John");
    client.setLastName("Doe");
    Client saveClient = clientRepository.save(client).block();
    AddressDto addressDto = new AddressDto();
    addressDto.setCity("city");
    addressDto.setStreet("street");
    CreateOrderRequest createOrderRequest = new CreateOrderRequest();
    createOrderRequest.setClientId(saveClient.getId());
    createOrderRequest.setNumberOfPilots(17);
    createOrderRequest.setAddress(addressDto);
    // When
    webTestClient.post()
        .uri("/orders").contentType(MediaType.APPLICATION_JSON)
        .bodyValue(createOrderRequest)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  public void getOrders_shouldReturnOrdersByFilter() {
    // Given
    Client client1 = new Client();
    client1.setFirstName("bla");
    client1.setLastName("bla");
    Client saveClient1 = clientRepository.save(client1).block();
    Client client2 = new Client();
    client2.setFirstName("John");
    client2.setLastName("Doe");
    Client saveClient2 = clientRepository.save(client2).block();

    Order order1 = new Order();
    order1.setClientFk(client1.getId());
    order1.setOrderTotal(new BigDecimal("2.66"));
    order1.setPilotes(5);
    orderRepository.save(order1).block();
    Order order2 = new Order();
    order2.setClientFk(client2.getId());
    order2.setOrderTotal(new BigDecimal("3.99"));
    order2.setPilotes(15);
    Order expectedOrder = orderRepository.save(order2).block();
    // When
    webTestClient.get()
        .uri("/orders?userNamePrefix=John")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.orders.length()").isEqualTo(1)
        .jsonPath("$.orders[0].id").isEqualTo(expectedOrder.getId())
        .jsonPath("$.orders[0].orderTotal").isEqualTo(3.99)
        .jsonPath("$.orders[0].pilotes").isEqualTo(15);
  }
}
