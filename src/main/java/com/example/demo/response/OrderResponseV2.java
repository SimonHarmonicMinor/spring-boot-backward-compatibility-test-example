package com.example.demo.response;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Value;

@Value
public class OrderResponseV2 {

  @NotNull
  Long id;
  @NotNull
  String name;
  @NotNull
  // В ответе новое поле можно объявлять как notnull, в отличие от запроса.
  // Потому что клиент про него не знает, следовательно, никак на него не завязан.
  List<String> items;
}
