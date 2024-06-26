package me.kupchenko.webfluxdemo.dto.request;

import lombok.Data;

@Data
public class CreateClientRequest {
  private String firstName;
  private String lastName;
  private String phoneNumber;
}
