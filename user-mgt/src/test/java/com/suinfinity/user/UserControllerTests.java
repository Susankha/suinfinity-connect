package com.suinfinity.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suinfinity.user.config.SecurityConfig;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@WithMockUser(username = "USER", roles = "ADMIN")
@DisplayName("User Operations Test Suite")
public class UserControllerTests {

  private static final String TEST_USER = "test_user";
  private static final String NEW_TEST_USER = "new_test_user";
  private static final String USER = "admin";
  private static final String ROLE = "ADMIN";
  private static final String PASSWORD = "Ad$n8admin";
  private static final String EMAIL = "admin@test.mail";
  private static final boolean IS_ENABLE = true;
  @MockitoBean UserService userService;
  @Autowired MockMvc mockMvc;
  @Autowired WebApplicationContext webApplicationContext;
  PasswordEncoder passwordEncoder;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    passwordEncoder = new BCryptPasswordEncoder();
  }

  @Test
  public void registerUser_shouldReturns_statusCreated() throws Exception {
    UserDTO userDTO = new UserDTO(TEST_USER, PASSWORD, ROLE, EMAIL, IS_ENABLE, getAddress());
    String jsonRequest = getJsonPayload(userDTO);
    Mockito.when(userService.registerUser(userDTO))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/users")
                .with(httpBasic(USER, PASSWORD))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  public void getUser_shouldReturns_User() throws Exception {
    User user = this.getUser();
    UserResponseDTO userResponseDto = UserMapper.INSTANCE.toUserResponseDto(user);
    Mockito.when(userService.getUser(user.getName()))
        .thenReturn(ResponseEntity.ok().body(userResponseDto));
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/v1/users/{user}", user.getName())
                .with(httpBasic(USER, PASSWORD))
                .with(csrf()))
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
        .perform(
            MockMvcRequestBuilders.get("/v1/users").with(httpBasic(USER, PASSWORD)).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
  }

  @Test
  public void updateUser_shouldReturns_updatedUser() throws Exception {
    UserDTO userDTO = new UserDTO(NEW_TEST_USER, PASSWORD, ROLE, EMAIL, IS_ENABLE, getAddress());
    String payload = getJsonPayload(userDTO);
    User user = UserMapper.INSTANCE.toUser(userDTO);
    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.toUserResponseDto(user);
    Mockito.when(userService.updateUser(anyString(), any(UserDTO.class)))
        .thenReturn(ResponseEntity.ok().body(userResponseDTO));
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/v1/users/{user}", user.getName())
                .with(httpBasic(USER, PASSWORD))
                .with(csrf())
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
        .perform(
            MockMvcRequestBuilders.delete("/v1/users/{user}", userName)
                .with(httpBasic(USER, PASSWORD))
                .with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  private User getUser() {
    User user = new User();
    user.setUserId(RandomGenerator.getDefault().nextLong());
    user.setName(TEST_USER);
    user.setPassword(PASSWORD);
    user.setRole(ROLE);
    user.setEmail(EMAIL);
    user.setIsEnable(IS_ENABLE);
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
