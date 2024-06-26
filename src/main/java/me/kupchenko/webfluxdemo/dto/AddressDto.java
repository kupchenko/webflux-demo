package me.kupchenko.webfluxdemo.dto;

import lombok.Data;

@Data
public class AddressDto {
  private String street;
  private String postcode;
  private String city;
  private String country;
}
