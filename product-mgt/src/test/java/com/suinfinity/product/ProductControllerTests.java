package com.suinfinity.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suinfinity.product.controller.ProductController;
import com.suinfinity.product.dto.ProductDTO;
import com.suinfinity.product.dto.ProductResponseDTO;
import com.suinfinity.product.mapper.ProductMapper;
import com.suinfinity.product.model.Product;
import com.suinfinity.product.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Product Operations Test Suite")
public class ProductControllerTests {

  private static final String TEST_PRODUCT = "test_product";
  private static final String NEW_TEST_PRODUCT = "new_test_product";
  private static final String TEST_DESCRIPTION = "test_desc";
  private static final BigDecimal TEST_PRICE = BigDecimal.valueOf(12.45);
  private static final long TEST_STOCK_QUANTITY = 100;

  @MockitoBean ProductService productService;
  @Autowired MockMvc mockMvc;

  @Test
  public void registerProduct_shouldReturns_statusCreated() throws Exception {
    ProductDTO productDTO = getProductDTO();
    String jsonRequest = getJsonPayload(productDTO);
    Mockito.when(productService.registerProduct(productDTO))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  public void getProduct_shouldReturns_Product() throws Exception {
    Product product = getProduct();
    ProductResponseDTO productResponseDTO = ProductMapper.INSTANCE.toProductResponseDTO(product);
    Mockito.when(productService.getProduct(product.getName()))
        .thenReturn(ResponseEntity.ok().body(productResponseDTO));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/products/{name}", product.getName()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.startsWith("test")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.stockQuantity").value(25));
  }

  @Test
  public void getProducts_shouldReturns_products() throws Exception {
    List<ProductResponseDTO> productResponseDTOS =
        Stream.of(getProduct()).map(ProductMapper.INSTANCE::toProductResponseDTO).toList();
    Mockito.when(productService.getProducts())
        .thenReturn(ResponseEntity.ok().body(productResponseDTOS));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/products"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
  }

  @Test
  public void updateProducts_shouldReturns_updatedProduct() throws Exception {
    ProductDTO productDTO = getProductDTO();
    String jsonPayload = getJsonPayload(productDTO);
    Product product = ProductMapper.INSTANCE.toProduct(productDTO);
    product.setName(NEW_TEST_PRODUCT);
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());
    product.setStockQuantity(productDTO.getStockQuantity());
    ProductResponseDTO productResponseDTO = ProductMapper.INSTANCE.toProductResponseDTO(product);
    Mockito.when(productService.updateProduct(NEW_TEST_PRODUCT, productDTO))
        .thenReturn(ResponseEntity.ok().body(productResponseDTO));
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/v1/products/{name}", product.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.startsWith("new_test")));
  }

  private ProductDTO getProductDTO() {
    return new ProductDTO(TEST_PRODUCT, TEST_DESCRIPTION, TEST_PRICE, TEST_STOCK_QUANTITY);
  }

  private Product getProduct() {
    Product product = new Product();
    product.setId(RandomGenerator.getDefault().nextLong());
    product.setName(TEST_PRODUCT);
    product.setDescription("product_desc");
    product.setPrice(BigDecimal.valueOf(12.25));
    product.setStockQuantity(25);
    return product;
  }

  private String getJsonPayload(Object product) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(product);
  }
}
