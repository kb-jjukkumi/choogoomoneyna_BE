package com.choogoomoneyna.choogoomoneyna_be.user.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MatchedUserVO {

    private Long id;
    private String nickname;
    private String profileImageUrl;
    private int userScore;
}
