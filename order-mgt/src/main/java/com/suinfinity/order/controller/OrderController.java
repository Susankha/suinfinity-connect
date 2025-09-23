package com.suinfinity.order.controller;

import com.suinfinity.order.dto.OrderDTO;
import com.suinfinity.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired private OrderService orderService;

  @PostMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> placeOrder(OrderDTO orderDTO) {
    System.out.println("AAAAA "+orderDTO.toString());
    return orderService.placeOrder(orderDTO);
  }
}
