package com.suinfinity.order.mapper;

import com.suinfinity.order.dto.OrderItemDTO;
import com.suinfinity.order.dto.OrderItemResponseDTO;
import com.suinfinity.order.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OrderItemMapper {

  OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

  OrderItem toOrderItem(OrderItemDTO orderItemDTO);

  OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem);
}
