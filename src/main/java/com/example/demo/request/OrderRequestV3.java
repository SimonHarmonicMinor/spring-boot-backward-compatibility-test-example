package com.example.demo.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// остается таким же, как OrderRequestV2
public class OrderRequestV3 {

  @NotNull
  private String name;
  @Nullable
  private List<String> items;
}
