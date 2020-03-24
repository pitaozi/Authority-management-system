package com.taoyuan.controller;

import com.taoyuan.common.JsonData;
import com.taoyuan.dto.DeptLevelDto;
import com.taoyuan.param.DeptParam;
import com.taoyuan.service.SysDeptService;
import com.taoyuan.service.SysDeptTreeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门 Controller 接口
 * @ProjectName permission
 * @ClassName SysDeptController
 * @Date 2019/12/13 13:40
 * @Author taoyuan
 * @Version 1.0
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Resource
    private SysDeptService sysdeptService;
    @Resource
    private SysDeptTreeService sysDeptTreeService;

    @RequestMapping("/save.json")
    @ResponseBody // 需要返回信息
    public JsonData saveDept (DeptParam deptParam) {
        sysdeptService.saveDept(deptParam);
        return JsonData.success(null,"当前部门保存成功");
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree () {
        List<DeptLevelDto> dtoList = sysDeptTreeService.deptTree();
        return JsonData.success(dtoList, "获取部门列表成功");
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept (DeptParam deptParam) {
        sysdeptService.updateDept(deptParam);
        return JsonData.success(null, "部门信息更新成功");
    }

    @RequestMapping("/dept.page")
    public ModelAndView page () {
        return new ModelAndView("dept");
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteDept (@Param("id") Integer id) {
        sysdeptService.deleteDept(id);
        return JsonData.success(null, "删除部门信息成功");
    }
}
