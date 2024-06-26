package me.kupchenko.webfluxdemo.dto.request;

import lombok.Data;

@Data
public class SearchOrdersRequest {
  private String userNamePrefix;
}
