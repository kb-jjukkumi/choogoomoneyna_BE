package com.choogoomoneyna.choogoomoneyna_be.user.mapper;


import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(UserVO user);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email, String loginType);

    UserVO findByEmail(String email);

    UserVO findByEmailAndLoginType(String email, String loginType);

    UserVO findByNickname(String nickname);

    UserVO findById(Long id);

    void updateUser(UserVO user);

    void deleteByEmail(String email);

    int countAllUsers();
}
