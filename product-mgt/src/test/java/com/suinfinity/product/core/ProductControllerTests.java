package com.suinfinity.product.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suinfinity.product.core.controller.ProductController;
import com.suinfinity.product.core.dto.ProductDTO;
import com.suinfinity.product.core.service.ProductService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

  private static final String TEST_PRODUCT = "test_product";
  private static final String TEST_DESCRIPTION = "test_desc";
  private static final BigDecimal TEST_PRICE = BigDecimal.valueOf(12.45);
  private static final long TEST_STOCK_QUANTITY = 100;

  @MockitoBean ProductService productService;
  @Autowired MockMvc mockMvc;

  @Test
  public void registerProduct_shouldReturns_statusCreated() throws Exception {
    ProductDTO productDTO =
        new ProductDTO(TEST_PRODUCT, TEST_DESCRIPTION, TEST_PRICE, TEST_STOCK_QUANTITY);
    String jsonRequest = getJsonPayload(productDTO);
    Mockito.when(productService.registerProduct(productDTO))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  private String getJsonPayload(Object product) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(product);
  }
}
