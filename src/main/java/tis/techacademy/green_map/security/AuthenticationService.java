package tis.techacademy.green_map.security;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tis.techacademy.green_map.exception.user.UserAlreadyExistsException;
import tis.techacademy.green_map.model.user.Role;
import tis.techacademy.green_map.model.user.UserEntity;
import tis.techacademy.green_map.repository.UserRepository;
import tis.techacademy.green_map.service.user.UserService;
import tis.techacademy.green_map.util.authentication.JwtAuthenticationResponse;
import tis.techacademy.green_map.util.authentication.SignInRequest;
import tis.techacademy.green_map.util.authentication.SignUpRequest;
import tis.techacademy.green_map.util.user.EmailUtil;
import tis.techacademy.green_map.util.user.OtpUtil;
import tis.techacademy.green_map.util.user.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserMapper USER_MAPPER;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;

    /**
     * User registration
     * Registers a new user by creating a user entity and storing it in the repository.
     * If the username is already taken, a UserAlreadyExistsException is thrown.
     * Upon successful registration, a JWT token is generated and returned.
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        String otp = otpUtil.generateOtp();

        var user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .otp(otp)
                .build();

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken.");
        }

        try {
            emailUtil.sendOtpEmail(request.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }

        userService.createUser(USER_MAPPER.entityToRequest(user));

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * User authentication
     * Authenticates a user based on the provided credentials.
     * Validates the username and password using the authentication manager.
     * Upon successful authentication, a JWT token is generated and returned.
     *
     * @param request user data
     * @return token
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        UserEntity userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow();
        if (!userEntity.isActive()) {
            throw new RuntimeException("User is not verified");
        }

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
