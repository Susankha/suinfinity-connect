package com.suinfinity.order.controller;

import com.suinfinity.order.dto.OrderDTO;
import com.suinfinity.order.dto.OrderResponseDTO;
import com.suinfinity.order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired private OrderService orderService;

  @PostMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderDTO orderDTO) {
    return orderService.placeOrder(orderDTO);
  }

  @GetMapping(value = "/get/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OrderResponseDTO> getOrder(@NotNull @PathVariable long orderId) {
    return orderService.getOrder(orderId);
  }

  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<OrderResponseDTO>> getOrders() {
    return orderService.getOrders();
  }

  @PatchMapping(value = "/update/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OrderResponseDTO> updateOrder(
      @NotNull @PathVariable long orderId, @RequestBody OrderDTO orderDTO) {
    return orderService.updateOrder(orderId, orderDTO);
  }

  @DeleteMapping(value = "/delete/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteOrder(@NotNull @PathVariable long orderId) {
    return orderService.deleteOrder(orderId);
  }
}
