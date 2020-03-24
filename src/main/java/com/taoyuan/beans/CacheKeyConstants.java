package com.taoyuan.beans;

import lombok.Getter;

/**
 * @ProjectName permission
 * @ClassName CacheKeyConstants
 * @Date 2020/3/11 22:24
 * @Author taoyuan
 * @Version 1.0
 */
@Getter
public enum CacheKeyConstants {

    //系统级别
    SYSTEM_ACLS,

    //用户级别，需要拼接，例如用户id
    USER_ACLS;
}
