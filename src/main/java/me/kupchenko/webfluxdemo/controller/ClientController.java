package me.kupchenko.webfluxdemo.controller;

import lombok.RequiredArgsConstructor;
import me.kupchenko.webfluxdemo.dto.request.CreateClientRequest;
import me.kupchenko.webfluxdemo.model.Client;
import me.kupchenko.webfluxdemo.service.ClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
  private final ClientService clientService;

  @PostMapping
  public Mono<Client> createClient(@RequestBody CreateClientRequest request) {
    return clientService.createClient(request);
  }
}
