package tis.techacademy.green_map.service.user;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tis.techacademy.green_map.controller.model.user.UserRequest;
import tis.techacademy.green_map.exception.authentication.ExpiredTokenException;
import tis.techacademy.green_map.exception.authentication.ForgotPasswordCouldNotSendEmailException;
import tis.techacademy.green_map.exception.authentication.InvalidTokenException;
import tis.techacademy.green_map.exception.user.ChangeRoleException;
import tis.techacademy.green_map.exception.user.UserAlreadyExistsException;
import tis.techacademy.green_map.model.user.Role;
import tis.techacademy.green_map.model.user.UserEntity;
import tis.techacademy.green_map.repository.UserRepository;
import tis.techacademy.green_map.util.user.EmailUtil;
import tis.techacademy.green_map.util.user.UserMapper;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailUtil emailUtil;

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    @CacheEvict(value = "users", key = "#email")
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    @CachePut(value = "users", key = "#newUser.username")
    public UserEntity createUser(UserRequest newUser) {

        if (userRepository.existsByUsername(newUser.getUsername()) || userRepository.existsByEmail(newUser.getEmail())) {
            log.error("User with username {} already exists", newUser.getUsername());
            throw new UserAlreadyExistsException("User with username " + newUser.getUsername() + " already exists");
        }
        UserDTO userDTO = userMapper.requestToDto(newUser);

        return userRepository.save(userMapper.dtoToEntity(userDTO));
    }

    @Override
    public List<UserEntity> listAllUsers() {
        return userRepository.findAll();
    }

    public String forgotPassword(String email) {
        log.info("Checking if user with email {} exists", email);
        userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        try {
            emailUtil.sendSetPasswordEmail(email);
        } catch (MessagingException e) {
            log.error("Unable to send email", e);
            throw new ForgotPasswordCouldNotSendEmailException("Unable to send email");
        }
        return "Please check your email to set new password to your account " + email;
    }

    public String setPassword(String email, String password, String token) {
        log.info("Checking if user with email {} exists", email);
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info("Checking if token is valid");
        if (!user.getToken().equals(token)) {
            throw new InvalidTokenException("Invalid token");
        }
        if (Instant.now().isAfter(user.getTokenCreatedAt().plusSeconds(60 * 5))) {
            throw new ExpiredTokenException("Token expired");
        }

        user.setPassword(password);
        userRepository.save(user);
        return "Password changed successfully for user " + email;

    }

    public String verifyAccount(String email, String otp) {
        log.info("Checking if user with email {} exists", email);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        log.info("Checking if otp is valid");
        if (user.getOtp().equals(otp)) {
            user.setActive(true);
            userRepository.save(user);
            return "Account verified successfully";
        }
        return "Please regenerate otp and try again";
    }

    public UserEntity makeAdmin(String email) {
        var currentUser = getCurrentUser();
        log.info("Checking if current user is admin");
        if (currentUser.getRole() != Role.ADMIN) {
            throw new ChangeRoleException("Only admin can change user role");
        }
        log.info("Checking if user with email {} exists", email);
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public UserEntity getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
