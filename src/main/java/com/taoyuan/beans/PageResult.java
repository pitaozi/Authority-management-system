package com.taoyuan.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @ProjectName permission
 * @ClassName PageResult
 * @Date 2019/12/18 10:37
 * @Author taoyuan
 * @Version 1.0
 */
@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {

    private List<T> data = Lists.newArrayList();

    private int total = 0;
}
