package com.example.demo.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequestV2 {

  // название заказа по-прежнему остается обязательным
  @NotNull
  private String name;
  @Nullable
  // Список товаров объявляем nullable, так как клиент может еще воспринимать наше API как V1.
  // Следовательно, он не знает, что теперь мы еще принимаем список items.
  // Так что нужно корректно обрабатывать ситуации, когда списка товаров нет.
  private List<String> items;
}
