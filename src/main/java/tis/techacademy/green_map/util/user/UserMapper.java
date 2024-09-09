package tis.techacademy.green_map.util.user;

import org.mapstruct.Mapper;
import tis.techacademy.green_map.controller.model.user.UserRequest;
import tis.techacademy.green_map.model.user.UserEntity;
import tis.techacademy.green_map.service.user.UserDTO;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity dtoToEntity(UserDTO userDTO);

    UserDTO requestToDto(UserRequest userRequest);

    UserEntity requestToEntity(UserRequest userRequest);

    UserRequest entityToRequest(UserEntity userEntity);
}
