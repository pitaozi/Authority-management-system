package com.taoyuan.common;

import com.taoyuan.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理类
 * @ProjectName permission
 * @ClassName SpringExceptionResolver
 * @Date 2019/12/12 9:28
 * @Author taoyuan
 * @Version 1.0
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        /**
         * 获取当前请求地址
         */
        String url = request.getRequestURL().toString();
        /**
         * 定义一个视图解析器
         */
        ModelAndView modelAndView;
        /**
         * 已知异常的默认消息
         */
        String defaultMessage = "System Error!";
        /**
         * 接下来我们对我们的数据和页面请求分别做错误处理
         * 总的提供两种方案
         * 第一种方案：从request中获取header(请求头)，看其具体是文本请求还是页面请求
         * 第二种方案：直接通过接口实现，我们定义所有的json请求以json结尾；页面请求
         * 那么我们通过接口就可以清晰的知道我们这块是数据请求还是页面请求
         * 接下来我们实现以json请求的处理
         */
        if(url.endsWith(".json")) {
            log.info("当前请求时JSON请求");
            log.warn("警告异常");
            // instanceof 用来测试一个对象是否为一个类的实例
            if (ex instanceof PermissionException) {
                // 已知异常类，这个异常是我们的系统异常
                log.error("这是一个已知异常类，PermissionException；url:" + url + "\texception:" + ex);
                JsonData result = JsonData.fail(ex.getMessage());
                modelAndView = new ModelAndView("jsonView", result.toMap());
            } else {
                // 出现了未知异常
                log.error("这是一个未知JSON请求异常，url:" + url + "\texception:" + ex);
                JsonData result = JsonData.fail(defaultMessage);
                modelAndView = new ModelAndView("jsonView", result.toMap());
            }
        } else if (url.endsWith(".page")) {
            log.error("这是一个未知的page异常，url：" + url + "\texception：" + ex);
            JsonData result = JsonData.fail(defaultMessage);
            modelAndView = new ModelAndView("exception", result.toMap());
        } else {
            log.error("这是一个未知异常，url：" + url + "\texception：" + ex);
            JsonData result = JsonData.fail(defaultMessage);
            modelAndView = new ModelAndView("jsonView", result.toMap());
        }
        return modelAndView;
    }
}
