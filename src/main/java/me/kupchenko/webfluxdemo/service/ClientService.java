package me.kupchenko.webfluxdemo.service;

import lombok.RequiredArgsConstructor;
import me.kupchenko.webfluxdemo.dto.request.CreateClientRequest;
import me.kupchenko.webfluxdemo.model.Client;
import me.kupchenko.webfluxdemo.repository.ClientRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClientService {
  private final ClientRepository clientRepository;

  public Mono<Client> createClient(CreateClientRequest request) {
    Client client = new Client();
    client.setFirstName(request.getFirstName());
    client.setLastName(request.getLastName());
    client.setTelephone(request.getPhoneNumber());
    return clientRepository.save(client);
  }
}
