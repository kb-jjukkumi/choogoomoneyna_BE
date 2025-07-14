package com.choogoomoneyna.choogoomoneyna_be.user.mapper;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.UserDTO;

public interface UserMapper {
    void insertUser(UserDTO user);

    UserDTO findByEmail(String email);

    UserDTO findByNickname(String nickname);

    UserDTO findById(Long id);

    void updateUser(UserDTO user);

    void deleteByEmail(String email);
}
