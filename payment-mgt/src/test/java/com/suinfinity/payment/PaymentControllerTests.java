package com.suinfinity.payment;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.suinfinity.payment.dto.PaymentDTO;
import com.suinfinity.payment.dto.PaymentResponseDTO;
import com.suinfinity.payment.mapper.PaymentMapper;
import com.suinfinity.payment.model.Payment;
import com.suinfinity.payment.service.PaymentService;
import com.suinfinity.payment.util.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Payment Operations Test Suite")
public class PaymentControllerTests {

  private static final BigDecimal AMOUNT = BigDecimal.valueOf(250);
  private static final String ORDER_ID = "80";
  private static final long PAYMENT_ID = 12;
  private static final String PAYMENT_METHOD_CASH = "cash";
  private static final String PAYMENT_METHOD_CARD = "card";
  private static final String PAYMENT_STATUS = "paid";

  @MockitoBean PaymentService paymentService;
  @Autowired MockMvc mockMvc;

  @Test
  @DisplayName("Make payment operation test")
  public void makePayment_shouldReturns_statusCreated() throws Exception {
    PaymentDTO paymentDTO = this.getPaymentDTO();
    String jsonRequest = getJsonPayload(paymentDTO);
    Mockito.when(paymentService.makePayment(paymentDTO))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @DisplayName("Get payment operation test")
  public void getPayment_shouldReturns_Payment() throws Exception {
    Payment payment = this.getPayment();
    PaymentResponseDTO paymentResponseDTO = PaymentMapper.INSTANCE.toPaymentResponseDTO(payment);
    Mockito.when(paymentService.getPayment(PAYMENT_ID))
        .thenReturn(ResponseEntity.ok(paymentResponseDTO));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/payments/{payment-id}", PAYMENT_ID))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.amount", equalTo(250)))
        .andExpect(jsonPath("$.paymentMethod", equalTo(PAYMENT_METHOD_CASH)));
  }

  @Test
  @DisplayName("Get payments operation test")
  public void getPayments_shouldReturns_Payments() throws Exception {
    List<Payment> payments = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      payments.add(getPayment());
    }
    List<PaymentResponseDTO> paymentResponseDTOS =
        payments.stream().map(PaymentMapper.INSTANCE::toPaymentResponseDTO).toList();
    Mockito.when(paymentService.getPayments()).thenReturn(ResponseEntity.ok(paymentResponseDTOS));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/payments"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$", hasSize(5)));
  }

  @Test
  @DisplayName("Update payment operation test")
  public void updatePayment_shouldReturns_updatedPayment() throws Exception {
    PaymentDTO paymentDTO = this.getPaymentDTO();
    paymentDTO.setPaymentMethod(PAYMENT_METHOD_CARD);
    String jsonRequest = getJsonPayload(paymentDTO);
    Payment payment = PaymentMapper.INSTANCE.toPayment(paymentDTO);
    long paymentId = Math.abs(RandomGenerator.getDefault().nextLong());
    PaymentResponseDTO paymentResponseDTO = PaymentMapper.INSTANCE.toPaymentResponseDTO(payment);
    Mockito.when(paymentService.updatePayment(paymentId, paymentDTO))
        .thenReturn(ResponseEntity.ok().body(paymentResponseDTO));
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/v1/payments/{payment-id}", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.paymentMethod", equalTo(PAYMENT_METHOD_CARD)));
  }

  @Test
  @DisplayName("GetPaymentStatus operation test")
  public void getPaymentStatus_shouldReturns_paymentStatus() throws Exception {
    long paymentId = Math.abs(RandomGenerator.getDefault().nextLong());
    Mockito.when(paymentService.getPaymentStatus(paymentId))
        .thenReturn(ResponseEntity.ok().body(PaymentStatus.COMPLETED.toString()));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/payments/{payment-id}/status", paymentId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("COMPLETED"));
  }

  @Test
  @DisplayName("UpdatePaymentStatus operation test")
  public void updatePaymentStatus_shouldReturns_updatedStatus() throws Exception {
    long paymentId = Math.abs(RandomGenerator.getDefault().nextLong());
    Mockito.when(paymentService.updatePaymentStatus(paymentId, PaymentStatus.COMPLETED.toString()))
        .thenReturn(ResponseEntity.ok().build());
    mockMvc
        .perform(
            MockMvcRequestBuilders.patch(
                "/v1/payments/{payment-id}/status/{payment-status}", paymentId, "COMPLETED"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("Delete payment operation test")
  public void deletePayments_shouldReturns_statusDeleted() throws Exception {
    long paymentId = Math.abs(RandomGenerator.getDefault().nextLong());
    Mockito.when(paymentService.deletePayment(paymentId))
        .thenReturn(ResponseEntity.noContent().build());
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/v1/payments/{payment-id}", paymentId))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  private Payment getPayment() {
    Payment payment = new Payment();
    payment.setPaymentId(Math.abs(RandomGenerator.getDefault().nextLong()));
    payment.setPaymentDate(
        Date.from(
            Instant.now().atZone(ZoneOffset.UTC).toLocalDateTime().toInstant(ZoneOffset.UTC)));
    payment.setOrderId(Long.parseLong(ORDER_ID));
    payment.setAmount(AMOUNT);
    payment.setPaymentMethod(PAYMENT_METHOD_CASH);
    payment.setPaymentStatus(PAYMENT_STATUS);
    return payment;
  }

  private PaymentDTO getPaymentDTO() {
    return new PaymentDTO(ORDER_ID, AMOUNT, PAYMENT_METHOD_CASH);
  }

  private String getJsonPayload(Object order) throws JsonProcessingException {
    ObjectMapper orderMapper = new ObjectMapper();
    orderMapper.registerModule(new JavaTimeModule());
    return orderMapper.writeValueAsString(order);
  }
}
