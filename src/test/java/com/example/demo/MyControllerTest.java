package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
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
        Map.of(
            "name", "myOrder",
            "items", List.of("item1", "item2")
        ),
        Map.class
    );
    assertTrue(responseV3.getStatusCode().is2xxSuccessful(),
        "Unexpected status code: " + responseV3.getStatusCode());
    final var orderResponseV3 = responseV3.getBody();
    assertNotNull(orderResponseV3.get("id"));
    assertEquals("myOrder", orderResponseV3.get("name"));
    assertEquals(List.of("item1", "item2"), orderResponseV3.get("items"));
    assertEquals(
        List.of(
            Map.of("id", 1, "name", "item1"),
            Map.of("id", 1, "name", "item2")
        ),
        orderResponseV3.get("itemsV2")
    );

    // вызываем v3 так, как будто это v2
    final var responseV2 = rest.postForEntity(
        "/api/order/v3",
        Map.of(
            "name", "anotherOrder",
            "items", List.of("item1")
        ),
        Map.class
    );
    assertTrue(responseV2.getStatusCode().is2xxSuccessful(),
        "Unexpected status code: " + responseV2.getStatusCode());
    final var orderResponseV2 = responseV2.getBody();
    assertNotNull(orderResponseV2.get("id"));
    assertEquals("anotherOrder", orderResponseV2.get("name"));
    assertEquals(List.of("item1"), orderResponseV2.get("items"));

    // Вызываем v3 так, как будто это v1
    final var responseV1 = rest.postForEntity(
        "/api/order/v3",
        Map.of(
            "name", "some order"
        ),
        Map.class
    );
    assertTrue(responseV1.getStatusCode().is2xxSuccessful(),
        "Unexpected status code: " + responseV1.getStatusCode());
    final var orderResponseV1 = responseV1.getBody();
    assertNotNull(orderResponseV1.get("id"));
    assertEquals("some order", orderResponseV1.get("name"));
  }
}