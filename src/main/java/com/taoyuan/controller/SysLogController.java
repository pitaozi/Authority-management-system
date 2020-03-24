package com.taoyuan.controller;

import com.taoyuan.beans.PageQuery;
import com.taoyuan.common.JsonData;
import com.taoyuan.param.SearchLogParam;
import com.taoyuan.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @ProjectName permission
 * @ClassName SysLogController
 * @Date 2020/3/12 9:48
 * @Author taoyuan
 * @Version 1.0
 */
@Controller
@RequestMapping("/sys/log")
@Slf4j
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @RequestMapping("/log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData searchPage(SearchLogParam param, PageQuery page) {
        return JsonData.success(sysLogService.searchPageList(param, page));
    }

    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData recover(@RequestParam("id") int id) {
        sysLogService.recover(id);
        return JsonData.success();
    }
}
