package com.choogoomoneyna.choogoomoneyna_be.user.dto.request;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UserUpdatedChoogooMiDTO {
    @NotNull(message = "추구미는 필수입니다")
    ChoogooMi choogooMi;
}
