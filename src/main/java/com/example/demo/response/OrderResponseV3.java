package com.example.demo.response;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Value;

@Value
public class OrderResponseV3 {

  @NotNull
  Long id;
  @NotNull
  String name;
  @NotNull
  @Deprecated(forRemoval = true)
  // В старом поле хранятся заказы без id-шников. Но передавать в items List<ItemResponseV3> вместо List<String> мы не можем.
  // Это сломает обратную совместимость, так как старые клиенты еще могут думать, что здесь хранится обычный список строк.
  // Так что объявляем старое поле как deprecated и добавляем новое. Когда клиенты полностью откажутся от использования этого поля, его можно будет удалить.
  List<String> items;

  @NotNull
  List<ItemResponseV3> itemsV2;

  @Value
  public static class ItemResponseV3 {

    @NotNull
    Long id;
    @NotNull
    String name;
  }
}
