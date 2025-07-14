package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.UserDTO;
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
                .build();
    }

    // VO -> DTO
    public static UserDTO voToDto(UserVO vo) {
        if (vo == null)
            return null;

        UserDTO dto = new UserDTO();
        dto.setEmail(vo.getEmail());
        dto.setPassword(vo.getPassword());
        dto.setNickname(vo.getNickname());
        dto.setRegDate(vo.getRegDate());
        dto.setUpdateDate(vo.getUpdateDate());
        dto.setProfileImageUrl(vo.getProfileImageUrl());
        dto.setProfileImage(null);
        dto.setChoogooMi(ChoogooMi.valueOf(vo.getChoogooMi()));

        return dto;
    }
}
