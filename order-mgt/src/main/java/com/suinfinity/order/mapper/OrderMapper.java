package com.suinfinity.order.mapper;

import com.suinfinity.order.dto.OrderDTO;
import com.suinfinity.order.dto.OrderResponseDTO;
import com.suinfinity.order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OrderMapper {

  OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

  Order toOrder(OrderDTO orderDTO);

  OrderResponseDTO toOrderResponseDTO(Order order);
}
