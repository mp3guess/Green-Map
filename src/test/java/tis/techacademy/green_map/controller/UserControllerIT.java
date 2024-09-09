package tis.techacademy.green_map.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tis.techacademy.green_map.controller.model.user.UserRequest;
import tis.techacademy.green_map.model.user.Role;
import tis.techacademy.green_map.model.user.UserEntity;
import tis.techacademy.green_map.repository.UserRepository;
import tis.techacademy.green_map.util.authentication.SignInRequest;
import tis.techacademy.green_map.util.authentication.SignUpRequest;
import tis.techacademy.green_map.util.user.UserMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tis.techacademy.green_map.controller.AuthenticationControllerIT.asJsonString;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper USER_MAPPER;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testGetUserByEmail_Should_Return_User() throws Exception {
        //Given
        UserEntity user = UserEntity.builder()
                .username("testuser")
                .email("test@example.com")
                .yearOfBirth(2000)
                .password("testpassword")
                .role(Role.USER)
                .build();
        userRepository.findByEmail("test@example.com").ifPresent(userRepository::delete);
        userRepository.save(user);

        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/test@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testuser"));

        userRepository.findByEmail("test@example.com").ifPresent(userRepository::delete);
    }


    @Test
    void testGetUserByEmail_Should_Throw_Exception() throws Exception {
        userRepository.findByEmail("usernotfound@example.com").ifPresent(userRepository::delete);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/usernotfound@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @WithMockUser
    void testGetUserInfo_Should_Return_UserInfo() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/user-info")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"));
    }


    @Test
    void testGetAllUsers_Should_Return_All_Users() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void testDeleteUser_Should_Delete_User() throws Exception {
        //Given
        UserEntity user = UserEntity.builder()
                .username("testuser")
                .email("someemail")
                .build();
        userRepository.save(user);
        //When
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/someemail")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser_Should_Update_User() throws Exception {
        //Given
        UserRequest user = UserRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .yearOfBirth(2000)
                .password("testpassword")
                .role(Role.USER.name())
                .build();
        userRepository.save(USER_MAPPER.requestToEntity(user));
        UserRequest updatedUser = UserRequest.builder()
                .username("updateduser")
                .email("test@example.com")
                .yearOfBirth(2000)
                .password("testpassword")
                .role(Role.USER.name())
                .build();

        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("updateduser"));

    }

    @Test
    @WithMockUser
    void testCreateUser_Should_Create_User() throws Exception {
        //Given
        UserRequest user = UserRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .yearOfBirth(2000)
                .password("testpassword")
                .role(Role.ADMIN.name())
                .build();

        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testuser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "testuser6", roles = {"ADMIN"})
    void testChangeRole() throws Exception {
        //Given
        SignUpRequest signUpRequest = createSignUpRequest("testuser6", "password123", "ususertotest@email.com");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        UserEntity user = userRepository.findByEmail("ususertotest@email.com").orElseThrow();
        user.setRole(Role.ADMIN);
        user.setActive(true);
        userRepository.save(user);

        SignInRequest signInRequest = SignInRequest.builder()
                .username("testuser6")
                .password("password123")
                .build();

        var signInResult = mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String token = JsonPath.read(signInResult.getResponse().getContentAsString(), "$.token");

        //When
        //Then
        mockMvc.perform(put("/api/users/make-admin/ususertotest@email.com")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private SignUpRequest createSignUpRequest(String username, String password, String email) {
        return SignUpRequest.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }
}