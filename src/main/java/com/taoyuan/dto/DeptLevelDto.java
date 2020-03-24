package com.taoyuan.dto;

import com.google.common.collect.Lists;
import com.taoyuan.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @ProjectName permission
 * @ClassName DeptLevelDto
 * @Date 2019/12/16 8:34
 * @Author taoyuan
 * @Version 1.0
 */
@Setter
@Getter
@ToString
public class DeptLevelDto extends SysDept {

    private List<DeptLevelDto> deptList = Lists.newArrayList();

    public static DeptLevelDto adapt (SysDept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        // 拷贝dept信息，并将其放到dto中
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
