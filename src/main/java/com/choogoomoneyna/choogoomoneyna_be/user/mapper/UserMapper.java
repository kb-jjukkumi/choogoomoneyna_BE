package com.choogoomoneyna.choogoomoneyna_be.user.mapper;


import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    void insertUser(UserVO user);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(@Param("email") String email, @Param("loginType") String loginType);

    UserVO findByEmail(String email);

    UserVO findByEmailAndLoginType(@Param("email") String email, @Param("loginType") String loginType);

    UserVO findByNickname(String nickname);

    UserVO findById(Long id);

    String getChoogooMiByUserId(Long userId);

    String getNicknameByUserId(Long userId);

    void updateUser(UserVO user);

    void updateNicknameAndPasswordByUserId(
            @Param("userId") Long userId,
            @Param("nickname") String nickname,
            @Param("password") String password
    );

    void deleteByEmail(String email);

    int countAllUsers();

    void updateConnectedId(@Param("id") Long id, @Param("connectedId") String connectedId);

    List<UserVO> findAllUsers();

    void updatePasswordByUserEmail(@Param("email") String email, @Param("password") String password);
}
