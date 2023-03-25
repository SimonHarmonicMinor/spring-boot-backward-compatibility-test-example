package com.example.demo.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

// В первой версии заказа передаем только название
@Data
@AllArgsConstructor
public class OrderRequestV1 {
  @NotNull
  private String name;
}
