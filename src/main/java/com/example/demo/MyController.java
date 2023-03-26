package com.example.demo;

import com.example.demo.request.OrderRequestV1;
import com.example.demo.request.OrderRequestV2;
import com.example.demo.request.OrderRequestV3;
import com.example.demo.response.OrderResponseV1;
import com.example.demo.response.OrderResponseV2;
import com.example.demo.response.OrderResponseV3;
import com.example.demo.response.OrderResponseV3.ItemResponseV3;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Objects;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Каждый новый endpoint отображает инкремент. То есть то, как один конкретный endpoint бы меняться,
 * чтобы сохранять обратную совместимость.
 * <p>
 * Это значит, что к разным endpoint-ам v1, v2, v3 нужно относиться так, как будто менялся один и
 * тот же. Каждый инкремент объявлен отдельным endpoint-ом для наглядности.
 * <p>
 * Смотрите MyControllerTest для примера проверки обратной совместимости.
 */
@RestController
@Validated
public class MyController {

  // Информация о заказе передается в body.
  // Аннотация @Valid выполняет проверки внутри переданного DTO по аннотациям @NotNull
  @PostMapping("/api/order/v1")
  public OrderResponseV1 createOrder(@Valid @RequestBody OrderRequestV1 request) {
    return new OrderResponseV1(1L, request.getName());
  }

  // При создании заказа теперь можно сразу указывать список товаров, которые туда нужно добавить
  @PostMapping("/api/order/v2")
  public OrderResponseV2 createOrderV2(@Valid @RequestBody OrderRequestV2 request) {
    return new OrderResponseV2(
        1L,
        request.getName(),
        // список товаров, который передал пользователь, может быть равен null
        Objects.requireNonNullElse(request.getItems(), Collections.emptyList())
    );
  }

  // Хотим, чтобы в ответе возвращался список товаров вместе с их id-шниками
  @PostMapping("/api/order/v3")
  public OrderResponseV3 createOrderV3(@Valid @RequestBody OrderRequestV3 request) {
    final var items = Objects.requireNonNullElse(request.getItems(),
        Collections.<String>emptyList());
    return new OrderResponseV3(
        1L,
        request.getName(),
        // deprecated поле также нужно заполнять корректными значениями, потому что клиенты еще могут его читать
        items,
        items.stream()
            .map(item -> new ItemResponseV3(1L, item))
            .toList()
    );
  }
}
