package tis.techacademy.green_map.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tis.techacademy.green_map.model.user.UserEntity;
import tis.techacademy.green_map.repository.UserRepository;
import tis.techacademy.green_map.service.user.UserService;
import tis.techacademy.green_map.util.authentication.SignInRequest;
import tis.techacademy.green_map.util.authentication.SignUpRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIT {

    private final String URL_REGISTER = "/api/auth/register";

    private final SignUpRequest SIGN_UP_REQUEST = createSignUpRequest("testusertosignin", "password123", "testusertosignin@email.com");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
        mockMvc.perform(post(URL_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(SIGN_UP_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testSignUp_Success() throws Exception {
        //Given
        SignUpRequest request = createSignUpRequest("testuser6", "password123", "ususertotest@email.com");

        //When
        //Then
        mockMvc.perform(post(URL_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
        assertTrue(userRepository.findByUsername("testuser6").isPresent());
    }

    @Test
    public void testSignUp_InvalidEmail() throws Exception {
        //Given
        SignUpRequest request = createSignUpRequest("testuser", "password123", "invalidEmail");

        //When
        //Then
        mockMvc.perform(post(URL_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").doesNotExist());

    }

    @Test
    @WithMockUser
    void testSignIn_Success() throws Exception {
        //Given
        SignInRequest signInRequest = SignInRequest.builder()
                .username("testusertosignin")
                .password("password123")
                .build();

        UserEntity user = userRepository.findByUsername("testusertosignin").orElseThrow();
        user.setActive(true);
        userRepository.save(user);

        //When
        //Then
        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testSignIn_Failure() throws Exception {
        //Given
        SignInRequest signInRequest = SignInRequest.builder()
                .username("testnotusertosignin")
                .password("password123")
                .build();
        //When
        //Then
        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signInRequest)))
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    @WithMockUser
    void testForgotPassword() throws Exception {
        //Given
        String email = "testusertosignin@email.com";
        String responseMessage = "Please check your email to set new password to your account " + email;

        when(userService.forgotPassword(email)).thenReturn(responseMessage);

        //When
        //Then
        mockMvc.perform(put("/api/auth/forgot-password/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void testSetPassword_Should_Set_Password_Success() throws Exception {
        //Given
        SignUpRequest request = createSignUpRequest("testuser6", "password123", "ususertotest@email.com");

        //When
        mockMvc.perform(post(URL_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));

        mockMvc.perform(put("/api/auth/forgot-password/{email}", request.getEmail()));

        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = user.getToken();

        //Then
        mockMvc.perform(put("/api/auth/set-password/{email}/{token}?password=newpassword", user.getEmail(), token))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void testVerifyAccount_Should_Verify_Account() throws Exception {
        //Given
        SignUpRequest request = createSignUpRequest("testuser6", "password123", "ususertotest@email.com");

        //When
        //Then
        mockMvc.perform(post(URL_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
        assertTrue(userRepository.findByUsername("testuser6").isPresent());

        UserEntity user = userRepository.findByEmail("ususertotest@email.com").orElseThrow();
        String expectedResponse = "Account verified successfully";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/auth/verify-account")
                        .param("email", user.getEmail())
                        .param("otp", user.getOtp())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    @WithMockUser
    void testVerifyAccount_Should_Not_Verify_Account() throws Exception {
        //Given
        SignUpRequest request = createSignUpRequest("testuser6", "password123", "ususertotest@email.com");
        String expectedResponse = "Please regenerate otp and try again";

        //When
        //Then
        mockMvc.perform(post(URL_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        UserEntity user = userRepository.findByEmail("ususertotest@email.com").orElseThrow();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/auth/verify-account")
                        .param("email", user.getEmail())
                        .param("otp", "incorrectOtp")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }


    static String asJsonString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);

    }

    private SignUpRequest createSignUpRequest(String username, String password, String email) {
        return SignUpRequest.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }
}