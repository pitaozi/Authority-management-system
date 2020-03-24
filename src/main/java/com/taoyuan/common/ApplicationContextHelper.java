package com.taoyuan.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 全局上下文applicationContext管理类
 * @ProjectName permission
 * @ClassName ApplicationContextHelper
 * @Date 2019/12/13 9:48
 * @Author taoyuan
 * @Version 1.0
 */
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    /* 定义全局的 applicationContext */
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext (ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

    public static <T> T popBean (Class<T> tClass) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(tClass);
    }

    public static <T> T popBean (String name, Class<T> tClass) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, tClass);
    }
}
