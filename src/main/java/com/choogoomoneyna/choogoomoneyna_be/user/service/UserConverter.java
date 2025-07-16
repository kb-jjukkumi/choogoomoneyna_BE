package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.UserDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;

public class UserConverter {

    // DTO -> VO
    public static UserVO dtoToVo(UserDTO dto) {
        if (dto == null)
            return null;

        return UserVO.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .profileImageUrl(dto.getProfileImageUrl())
                .regDate(dto.getRegDate())
                .updateDate(dto.getUpdateDate())
                .choogooMi(dto.getChoogooMi().name())
                .loginType(dto.getLoginType().name())
                .build();
    }

    // VO -> DTO
    public static UserDTO voToDto(UserVO vo) {
        if (vo == null)
            return null;

        return UserDTO.builder()
                .email(vo.getEmail())
                .password(vo.getPassword())
                .nickname(vo.getNickname())
                .regDate(vo.getRegDate())
                .updateDate(vo.getUpdateDate())
                .profileImageUrl(vo.getProfileImageUrl())
                .profileImage(null)
                .choogooMi(ChoogooMi.valueOf(vo.getChoogooMi()))
                .loginType(LoginType.valueOf(vo.getLoginType()))
                .build();
    }

    // joinRequestDTO -> VO
    public static UserVO joinRequestDtoToVo(UserJoinRequestDTO dto, LoginType loginType, String encryptedPassword) {
        if (dto == null)
            return null;

        return UserVO.builder()
                .email(dto.getEmail())
                .password(encryptedPassword)
                .nickname(dto.getNickname())
//                .profileImageUrl(dto.getProfileImage() != null ? dto.getProfileImage().getOriginalFilename() : null)
                .choogooMi(dto.getChoogooMi().name())
                .loginType(loginType.name())
                .build();
    }
}
