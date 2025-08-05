package com.choogoomoneyna.choogoomoneyna_be.report.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportVO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Date regDate;
}
