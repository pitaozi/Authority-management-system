package com.taoyuan.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @ProjectName permission
 * @ClassName SearchLogDto
 * @Date 2020/3/12 17:21
 * @Author taoyuan
 * @Version 1.0
 */
@Getter
@Setter
@ToString
public class SearchLogDto {

    private Integer type;//logType

    private String beforeSeq;

    private String afterSeq;

    private String operator;

    private Date fromTime;//yyyy-MM-dd HH:mm:ss

    private Date toTime;//yyyy-MM-dd HH:mm:ss
}
