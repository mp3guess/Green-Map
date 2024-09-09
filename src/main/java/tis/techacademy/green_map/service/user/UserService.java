package tis.techacademy.green_map.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import tis.techacademy.green_map.controller.model.user.UserRequest;
import tis.techacademy.green_map.model.user.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity findUserByEmail(String email);

    void deleteUser(String email);

    UserEntity createUser(UserRequest newUser);

    List<UserEntity> listAllUsers();

    UserDetailsService userDetailsService();

    UserEntity makeAdmin(String email);

    Object getCurrentUser();

    String forgotPassword(String email);

    String setPassword(String email, String password, String token);

    String verifyAccount(String email, String otp);
}
