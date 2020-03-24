package com.taoyuan.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ProjectName permission
 * @ClassName SearchLogParam
 * @Date 2020/3/12 17:19
 * @Author taoyuan
 * @Version 1.0
 */
@Getter
@Setter
@ToString
public class SearchLogParam {

    private Integer type;//logType

    private String beforeSeq;

    private String afterSeq;

    private String operator;

    private String fromTime;//yyyy-MM-dd HH:mm:ss

    private String toTime;//yyyy-MM-dd HH:mm:ss
}
