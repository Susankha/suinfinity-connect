package com.susa.user.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.susa.user.core.controller.UserController;
import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.dto.UserDTO.Address;
import com.susa.user.core.model.User;
import com.susa.user.core.service.UserService;
import java.util.random.RandomGenerator;
import org.hamcrest.Matchers;
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
import org.springframework.web.servlet.resource.NoResourceFoundException;

@WebMvcTest(UserController.class)
public class UserControllerTests {

  @MockitoBean UserService userService;

  @Autowired MockMvc mockMvc;

  @Test
  public void registerUser_status_returnsOk() throws Exception {
    UserDTO userDTO = new UserDTO("test_user", getAddress());
    String jsonRequest = getJsonPayload(userDTO);

    Mockito.when(userService.registerUser(userDTO))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  public void getUser_status_returnsUser() throws Exception {

    User user = new User();
    user.setUserId(RandomGenerator.getDefault().nextLong());
    user.setName("test_user");
    user.setAddress(this.getAddress());

    Mockito.when(userService.getUser(user.getName())).thenReturn(ResponseEntity.ok().body(user));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/users/get/" + user.getName()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.startsWith("test")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.address.street", Matchers.notNullValue()));
  }

  private Address getAddress() {
    Address address = new Address();
    address.setStreet("test_street");
    address.setCity("test_city");
    address.setZipCode("12345");
    return address;
  }

  private String getJsonPayload(Object user) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(user);
  }
}
