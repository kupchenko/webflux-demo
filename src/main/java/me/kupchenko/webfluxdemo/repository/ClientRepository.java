package me.kupchenko.webfluxdemo.repository;

import me.kupchenko.webfluxdemo.model.Client;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ClientRepository extends ReactiveCrudRepository<Client, Long> {

  @Query("SELECT * from clients c WHERE c.first_name like concat(:firstNamePrefix,'%') OR c.last_name like concat(:lastNamePrefix,'%') ")
  Flux<Client> findByFirstNameStartsWithAndLastNameStartsWith(String firstNamePrefix, String lastNamePrefix);
}
