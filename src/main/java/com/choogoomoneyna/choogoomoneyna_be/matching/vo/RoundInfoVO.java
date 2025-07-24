package com.choogoomoneyna.choogoomoneyna_be.matching.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundInfoVO {
    private Long id;
    private Integer roundNumber;
    private Date startDate;
    private Date endDate;
}
