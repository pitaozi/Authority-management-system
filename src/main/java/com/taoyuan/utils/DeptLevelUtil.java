package com.taoyuan.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 部门级别计算工具类
 * @ProjectName permission
 * @ClassName DeptLevelUtil
 * @Date 2019/12/13 14:09
 * @Author taoyuan
 * @Version 1.0
 */
@Slf4j
public class DeptLevelUtil {
    /* 定义部门层级之间的分隔符 */
    private static final String SEPARATOR = ".";

    /* 定义总部门的级别 ID */
    public static final String ROOT = "0";

    /* */
    public static String calculateLevel (String parentLevel, Integer parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }

}
