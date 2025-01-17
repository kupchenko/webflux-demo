package me.kupchenko.webfluxdemo.repository;

import me.kupchenko.webfluxdemo.model.Address;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AddressRepository extends ReactiveCrudRepository<Address, Long> {
}
