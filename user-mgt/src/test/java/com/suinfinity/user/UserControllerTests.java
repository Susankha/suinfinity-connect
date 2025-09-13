package com.suinfinity.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suinfinity.user.controller.UserController;
import com.suinfinity.user.dto.UserDTO;
import com.suinfinity.user.dto.UserDTO.Address;
import com.suinfinity.user.dto.UserResponseDTO;
import com.suinfinity.user.mapper.UserMapper;
import com.suinfinity.user.model.User;
import com.suinfinity.user.service.UserService;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;
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

@WebMvcTest(UserController.class)
public class UserControllerTests {

  private static final String TEST_USER = "test_user";
  private static final String NEW_TEST_USER = "new_test_user";
  @MockitoBean UserService userService;
  @Autowired MockMvc mockMvc;

  @Test
  public void registerUser_shouldReturns_statusCreated() throws Exception {
    UserDTO userDTO = new UserDTO(TEST_USER, getAddress());
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
    UserResponseDTO userDTO = UserMapper.INSTANCE.toUserResponseDto(user);
    Mockito.when(userService.getUser(user.getName())).thenReturn(ResponseEntity.ok().body(userDTO));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/users/get/{user}", user.getName()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.startsWith("test")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.address.street", Matchers.notNullValue()));
  }

  @Test
  public void getUsers_shouldReturns_Users() throws Exception {
    List<UserResponseDTO> userResponseDTOS =
        Stream.of(getUser()).map(UserMapper.INSTANCE::toUserResponseDto).toList();
    Mockito.when(userService.getUsers()).thenReturn(ResponseEntity.ok(userResponseDTOS));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/users/all"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
  }

  @Test
  public void updateUser_shouldReturns_updatedUser() throws Exception {
    UserDTO userDTO = new UserDTO(TEST_USER, getAddress());
    String payload = getJsonPayload(userDTO);
    User user = UserMapper.INSTANCE.toUser(userDTO);
    user.setName(NEW_TEST_USER);
    user.setAddress(userDTO.getAddress());
    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.toUserResponseDto(user);
    Mockito.when(userService.updateUser(NEW_TEST_USER, userDTO))
        .thenReturn(ResponseEntity.ok().body(userResponseDTO));
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/users/update/{user}", user.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.startsWith("new_test")));
  }

  @Test
  public void deleteUser_shouldReturns_statusDeleted() throws Exception {
    String userName = TEST_USER;
    Mockito.when(userService.deleteUser(userName)).thenReturn(ResponseEntity.noContent().build());
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/users/delete/{user}", userName))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  private User getUser() {
    User user = new User();
    user.setUserId(RandomGenerator.getDefault().nextLong());
    user.setName(TEST_USER);
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
