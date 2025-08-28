package com.susa.user.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.susa.user.core.controller.UserController;
import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.dto.UserDTO.Address;
import com.susa.user.core.mapper.UserMapper;
import com.susa.user.core.model.User;
import com.susa.user.core.service.UserService;
import java.util.List;
import java.util.random.RandomGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult.Status;
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
  public void registerUser_shouldReturns_statusCreated() throws Exception {
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
  public void getUser_shouldReturns_User() throws Exception {
    User user = this.getUser();
    Mockito.when(userService.getUser(user.getName())).thenReturn(ResponseEntity.ok().body(user));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/users/get/{user}", user.getName()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.startsWith("test")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.address.street", Matchers.notNullValue()));
  }

  @Test
  public void getUsers_shouldReturns_Users() throws Exception {
    List<User> users = List.of(getUser());
    Mockito.when(userService.getUsers()).thenReturn(ResponseEntity.ok(users));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/users/all"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
  }

  @Test
  public void updateUser_shouldReturns_updatedUser() throws Exception {
    UserDTO userDTO = new UserDTO("test_user", getAddress());
    UserMapper userMapper = new UserMapper();
    User user = userMapper.mapUserDTOtoUser(userDTO);
    String payload = getJsonPayload(userDTO);
    Mockito.when(userService.updateUser("new_test_user", userDTO))
        .thenReturn(ResponseEntity.ok().body(user));
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/users/update/{user}", user.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  private User getUser() {
    User user = new User();
    user.setUserId(RandomGenerator.getDefault().nextLong());
    user.setName("test_user");
    user.setAddress(this.getAddress());
    return user;
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
