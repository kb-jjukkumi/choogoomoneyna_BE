package com.choogoomoneyna.choogoomoneyna_be.match.service;

import com.choogoomoneyna.choogoomoneyna_be.user.vo.MatchedUserVO;

import java.util.List;

public interface MatchService {

    /**
     * 사용자를 둘 씩 매칭합니다
     *
     * @return 매칭된 사용자 쌍들의 리스트. 각 내부 리스트는 매칭된 두 사용자의 정보를 포함합니다.
     */
    public List<List<MatchedUserVO>> getUserPairs();
}
