package com.taoyuan.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @ProjectName permission
 * @ClassName DeptParam
 * @Date 2019/12/13 13:41
 * @Author taoyuan
 * @Version 1.0
 */
@Getter
@Setter
@ToString
public class DeptParam {

    private Integer id;

    @NotNull(message = "部门名称不能为空")
    @Length(max = 20, min = 2, message = "部门名称长度需要在2个字符到20个字符之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    @Length(max = 200, message = "备注长度不能超过200个字符长度")
    private String remark = "";
}
