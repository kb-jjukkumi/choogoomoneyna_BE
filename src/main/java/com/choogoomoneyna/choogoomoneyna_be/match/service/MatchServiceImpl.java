package com.choogoomoneyna.choogoomoneyna_be.match.service;

import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.MatchedUserVO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final UserMapper userMapper;

    @Override
    public List<List<MatchedUserVO>> getUserPairs() {
        List<UserVO> users = userMapper.findAllUsers();

        // id, email, profileImageUrl만 갖게 변환
        List<MatchedUserVO> matchableUsers = users.stream()
                .map(user -> new MatchedUserVO(user.getId(), user.getEmail(), user.getProfileImageUrl()))
                .toList();

        return null;
    }
}
