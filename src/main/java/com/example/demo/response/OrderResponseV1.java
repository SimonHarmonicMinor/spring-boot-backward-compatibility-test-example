package com.example.demo.response;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class OrderResponseV1 {
  // ID заказа генерируется при его создании
  @NotNull
  Long id;
  @NotNull
  String name;
}
