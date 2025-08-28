package com.susa.user.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.susa.user.core.controller.UserController;
import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.dto.UserDTO.Address;
import com.susa.user.core.service.UserService;
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

@WebMvcTest(UserController.class)
public class UserControllerTests {

  @MockitoBean UserService userService;

  @Autowired MockMvc mockMvc;

  @Test
  public void registerUser_status_returnsOk() throws Exception {
    Address address = new Address();
    address.setStreet("test_street");
    address.setCity("test_city");
    address.setZipCode("12345");
    UserDTO userDTO = new UserDTO("test_User", address);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(userDTO);

    Mockito.when(userService.registerUser(userDTO))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }
}
