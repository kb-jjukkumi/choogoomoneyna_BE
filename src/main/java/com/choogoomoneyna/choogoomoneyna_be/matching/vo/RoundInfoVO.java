package com.choogoomoneyna.choogoomoneyna_be.matching.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RoundInfoVO {
    private Long id;
    private Integer roundNumber;
    private Date startDate;
    private Date endDate;
}
