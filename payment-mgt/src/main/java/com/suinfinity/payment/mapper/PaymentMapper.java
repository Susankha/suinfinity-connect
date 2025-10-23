package com.suinfinity.payment.mapper;

import com.suinfinity.payment.dto.PaymentDTO;
import com.suinfinity.payment.dto.PaymentResponseDTO;
import com.suinfinity.payment.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PaymentMapper {

  PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

  Payment toPayment(PaymentDTO paymentDTO);

  PaymentResponseDTO toPaymentResponseDTO(Payment payment);
}
