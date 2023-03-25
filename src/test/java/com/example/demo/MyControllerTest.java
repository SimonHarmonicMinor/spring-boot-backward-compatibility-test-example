package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.request.OrderRequestV1;
import com.example.demo.request.OrderRequestV2;
import com.example.demo.request.OrderRequestV3;
import com.example.demo.response.OrderResponseV1;
import com.example.demo.response.OrderResponseV2;
import com.example.demo.response.OrderResponseV3;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MyControllerTest {

  @Autowired
  private TestRestTemplate rest;

  @Test
    // Пытаемся обратиться к V3 так, как будто это:
    // 1. v3
    // 2. v2
    // 3. v1
  void shouldRemainBackwardCompatibilityForV3() {
    // Вызываем v3 так, как будто это v3
    final var responseV3 = rest.postForEntity(
        "/api/order/v3",
        new OrderRequestV3("myOrder", List.of("item1", "item2")),
        OrderResponseV3.class
    );
    assertTrue(responseV3.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + responseV3.getStatusCode());
    final var orderResponseV3 = responseV3.getBody();
    assertNotNull(orderResponseV3.getId());
    assertEquals("myOrder", orderResponseV3.getName());
    assertEquals(List.of("item1", "item2"), orderResponseV3.getItems());
    assertEquals(
        List.of(
            new OrderResponseV3.ItemResponseV3(1L, "item1"),
            new OrderResponseV3.ItemResponseV3(1L, "item2")
        ),
        orderResponseV3.getItemsV2()
    );

    // вызываем v3 так, как будто это v2
    final var responseV2 = rest.postForEntity(
        "/api/order/v3",
        new OrderRequestV2("anotherOrder", List.of("item1")),
        OrderResponseV2.class
    );
    assertTrue(responseV2.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + responseV2.getStatusCode());
    final var orderResponseV2 = responseV2.getBody();
    assertNotNull(orderResponseV2.getId());
    assertEquals("anotherOrder", orderResponseV2.getName());
    assertEquals(List.of("item1"), orderResponseV2.getItems());

    final var responseV1 = rest.postForEntity(
        "/api/order/v3",
        new OrderRequestV1("some order"),
        OrderResponseV1.class
    );
    assertTrue(responseV1.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + responseV1.getStatusCode());
    final var orderResponseV1 = responseV1.getBody();
    assertNotNull(orderResponseV1.getId());
    assertEquals("some order", orderResponseV1.getName());
  }
}