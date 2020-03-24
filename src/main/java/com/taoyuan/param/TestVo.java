package com.taoyuan.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ProjectName permission
 * @ClassName TestVo
 * @Date 2019/12/13 9:07
 * @Author taoyuan
 * @Version 1.0
 */
@Getter
@Setter
public class TestVo {

    @NotNull(message = "id不能为空")
    @Max(value = 10, message = "id不能大于10")
    @Min(value = 0, message = "id至少要大于等于0")
    private Integer id;

    @NotBlank(message = "message不能为空")
    private String message;

}
