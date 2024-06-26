package me.kupchenko.webfluxdemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "clients")
public class Client {
  @Id
  private Long id;
  private String firstName;
  private String lastName;
  private String telephone;
}
