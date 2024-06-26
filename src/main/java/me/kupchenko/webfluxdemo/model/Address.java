package me.kupchenko.webfluxdemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "addresses")
public class Address {
  @Id
  private Long id;
  private String street;
  private String postcode;
  private String city;
  private String country;
}
