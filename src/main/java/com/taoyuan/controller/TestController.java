package com.taoyuan.controller;

import com.taoyuan.common.JsonData;
import com.taoyuan.exception.ParamException;
import com.taoyuan.exception.PermissionException;
import com.taoyuan.model.SysDept;
import com.taoyuan.param.DeptParam;
import com.taoyuan.param.TestVo;
import com.taoyuan.service.SysDeptService;
import com.taoyuan.utils.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ProjectName permission
 * @ClassName TestController
 * @Date 2019/12/11 9:12
 * @Author taoyuan
 * @Version 1.0
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/test1.json")
    @ResponseBody
    public JsonData test1 () {
        log.info("此处是我们成功调用Hello方法的时候");
        return JsonData.success("Hello, Permission!");
    }

    @RequestMapping("/test2.json")
    public JsonData test2 () {
        log.info("请求permissionException报错");
        throw new PermissionException("Fatal Error");
    }

    @RequestMapping("/test3.json")
    public JsonData test3 () {
        log.info("json请求未知异常报错");
        throw new RuntimeException("Fatal Error");
    }

    @RequestMapping("/test4.page")
    public JsonData test4 () {
        log.info("page页面请求报错");
        throw new RuntimeException("Fatal Error");
    }

    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate (TestVo vo) throws ParamException {
        log.info("validate.json");
        try {
            Map<String, String> map = BeanValidator.validateObject(vo);
            if (map != null && map.entrySet().size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    log.info("{} -> {}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {

        }
        return JsonData.success("Test Validate");
    }

}
