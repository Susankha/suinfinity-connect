package com.suinfinity.order.service;

import com.suinfinity.order.dto.OrderDTO;
import com.suinfinity.order.mapper.OrderMapper;
import com.suinfinity.order.model.Order;
import com.suinfinity.order.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OrderService {

  @Autowired private OrderRepository orderRepository;

  public ResponseEntity<?> placeOrder(OrderDTO orderDTO) {
    Order order = OrderMapper.INSTANCE.toOrder(orderDTO);
    orderRepository.save(order);
    return ResponseEntity.status(HttpStatus.CREATED).body("Order " + " successfully registered");
  }
}
