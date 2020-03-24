package com.taoyuan.controller;

import com.google.common.collect.Maps;
import com.taoyuan.beans.PageQuery;
import com.taoyuan.beans.PageResult;
import com.taoyuan.common.JsonData;
import com.taoyuan.model.SysUser;
import com.taoyuan.param.UserParam;
import com.taoyuan.service.SysRoleService;
import com.taoyuan.service.SysTreeService;
import com.taoyuan.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ProjectName permission
 * @ClassName SysUserController
 * @Date 2019/12/17 17:09
 * @Author taoyuan
 * @Version 1.0
 */
@Controller
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth() {
        return new ModelAndView("noAuth");
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser (UserParam userParam) {
        sysUserService.saveUser(userParam);
        return JsonData.success(null,"当前用户保存成功");
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser (UserParam userParam) {
        sysUserService.updateUser(userParam);
        return JsonData.success(null, "用户信息更新成功");
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@Param("deptId") Integer deptId, PageQuery pageQuery) {
        PageResult<SysUser> result = sysUserService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(result);
    }

    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }
}
