package tis.techacademy.green_map.service.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tis.techacademy.green_map.controller.model.user.UserRequest;
import tis.techacademy.green_map.exception.user.UserAlreadyExistsException;
import tis.techacademy.green_map.model.user.Role;
import tis.techacademy.green_map.model.user.UserEntity;
import tis.techacademy.green_map.repository.UserRepository;
import tis.techacademy.green_map.util.user.UserMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceImplTest {

    private static final UserEntity USER_ENTITY = UserEntity.builder()
            .id(1)
            .username("john_doe")
            .password("password123")
            .email("john@mail.com")
            .role(Role.USER)
            .yearOfBirth(1990)
            .build();

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl underTest;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testFindUserByEmail_Should_Return_UserEntity() {
        //Given
        String email = USER_ENTITY.getEmail();
        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(USER_ENTITY));

        //When
        var actual = underTest.findUserByEmail(email);

        //Then
        assertEquals(email, actual.getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindUserByEmail_Should_Return_Null() {
        //Given
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        //When
        //Then
        assertThrows(RuntimeException.class, () -> underTest.findUserByEmail(email));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testDeleteUser_Should_Delete_User() {
        //Given
        userRepository.save(USER_ENTITY);

        //When
        userRepository.deleteById(USER_ENTITY.getId());

        //Then
        verify(userRepository).deleteById(USER_ENTITY.getId());
    }

    @Test
    void testDeleteUser_Should_Not_Delete_User() {
        //Given
        userRepository.deleteById(USER_ENTITY.getId());

        // When
        //Then
        verify(userRepository).deleteById(USER_ENTITY.getId());
    }

    @Test
    void testCreateUser_Should_Return_User() {
        // Given
        UserRequest userToCreate = userRequest();

        UserDTO userDTO = new UserDTO();
        UserEntity userEntity = userEntity();

        when(userMapper.requestToDto(userToCreate)).thenReturn(userDTO);
        when(userMapper.dtoToEntity(userDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        // When
        UserEntity actual = underTest.createUser(userToCreate);

        // Then
        verify(userRepository).save(userEntity);
        assertEquals("some_email", actual.getEmail());
    }

    @Test
    void testCreateUser_Should_Not_Create_User() {
        //Given
        UserRequest userToCreate = userRequest();

        userRepository.save(userMapper.dtoToEntity(userMapper.requestToDto(userToCreate)));
        when(userRepository.existsByUsername(userToCreate.getUsername()))
                .thenReturn(true);
        // When
        assertThrows(UserAlreadyExistsException.class, () -> underTest.createUser(userToCreate));

        // Then
        verify(userRepository).save(userMapper.dtoToEntity(userMapper.requestToDto(userToCreate)));
    }

    @Test
    void listAllUsers() {
        //Given
        UserEntity user1 = UserEntity
                .builder()
                .build();


        UserEntity user2 = UserEntity
                .builder()
                .build();

        List<UserEntity> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        // When
        var actual = underTest.listAllUsers();

        // Then
        Assertions.assertNotNull(actual);
        assertTrue(actual.containsAll(userList));
    }

    private UserRequest userRequest() {
        return UserRequest.builder()
                .username("john_doe")
                .password("password123")
                .email("some_email")
                .build();
    }

    private UserEntity userEntity() {
        return UserEntity.builder()
                .username("john_doe")
                .password("password123")
                .email("some_email")
                .role(Role.USER)
                .yearOfBirth(1990)
                .build();
    }
}