package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.enums.MatchingResult;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.response.UserMainResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.response.UserMatchingResultHistoryDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.UserDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMainInfoVO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMatchingResultHistoryVO;
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
    public static UserVO joinRequestDtoToVo(UserJoinRequestDTO dto, LoginType loginType, String encryptedPassword, ChoogooMi choogooMi) {
        if (dto == null)
            return null;

        return UserVO.builder()
                .email(dto.getEmail())
                .password(encryptedPassword)
                .nickname(dto.getNickname())
//                .profileImageUrl(dto.getProfileImage() != null ? dto.getProfileImage().getOriginalFilename() : null)
                .choogooMi(choogooMi.name())
                .loginType(loginType.name())
                .build();
    }

    // UserMainVo -> UserMainResponseDTO
    public static UserMainResponseDTO toUserMainResponseDTO(UserMainInfoVO userMainInfoVo) {
        return UserMainResponseDTO.builder()
                .choogooMi(ChoogooMi.valueOf(userMainInfoVo.getChoogooMi()))
                .nickname(userMainInfoVo.getNickname())
                .userScore(userMainInfoVo.getUserScore())
                .userRanking(userMainInfoVo.getUserRanking())
                .isLevelUp(userMainInfoVo.getIsLevelUp())
                .build();
    }

    // UserMatchingResultHistoryVO -> UserMatchingResultHistoryDTO
    public static UserMatchingResultHistoryDTO toUserMatchingResultHistoryDTO(UserMatchingResultHistoryVO userMatchingResultHistoryVO) {
        return UserMatchingResultHistoryDTO.builder()
                .roundNumber(userMatchingResultHistoryVO.getRoundNumber())
                .startDate(userMatchingResultHistoryVO.getStartDate())
                .endDate(userMatchingResultHistoryVO.getEndDate())
                .matchingResult(MatchingResult.valueOf(userMatchingResultHistoryVO.getMatchingResult()))
                .build();
    }
}
