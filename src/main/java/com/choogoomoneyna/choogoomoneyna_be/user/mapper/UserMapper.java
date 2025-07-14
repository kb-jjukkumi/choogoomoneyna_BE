package com.choogoomoneyna.choogoomoneyna_be.user.mapper;


import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;

public interface UserMapper {
    void insertUser(UserVO user);

    UserVO findByEmail(String email);

    UserVO findByNickname(String nickname);

    UserVO findById(Long id);

    void updateUser(UserVO user);

    void deleteByEmail(String email);
}
