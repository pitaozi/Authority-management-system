package com.taoyuan.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taoyuan.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * Validator 校验工具
 * @ProjectName permission
 * @ClassName BeanValidator
 * @Date 2019/12/13 8:29
 * @Author taoyuan
 * @Version 1.0
 */
public class BeanValidator {

    /**
     * 定义一个全局校验工厂
     */
    private static ValidatorFactory validatorFactor = Validation.buildDefaultValidatorFactory();

    /**
     * 定义一个普通的校验方法，可以放入不同类型各种类
     * @param t 泛型
     * @param groups 可变长参数
     * @param <T>
     * @return
     */
    public <T> Map<String, String> validate (T t, Class... groups) {
        /**
         * 从工厂获取到当前的Validator
         */
        Validator validator = validatorFactor.getValidator();
        /**
         * 使用validator中的validate方法校验我们传入的t，groups
         */
        Set<ConstraintViolation<T>> validateResult = validator.validate(t, groups);
        /**
         * 判断当前的校验结果，如果校验结果为空，就返回一个空的Map
         */
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            /**
             * 如果校验结果不为空，就代表现在里面出现错误了
             */
            // HashMap是无序的，LinkedHashMap是有序的，且默认为插入顺序
            LinkedHashMap errors = Maps.newLinkedHashMap();
            /**
             * 接下来遍历我们的 result
             */
            Iterator iterator = validateResult.iterator();
            /**
             * iterator.hasNext() 判断当前元素是否存在，并没有指向
             */
            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    public Map<String, String> validateList (Collection<?> collection) {
        /* 校验collection是否为空 */
        Preconditions.checkNotNull(collection);
        /* 遍历collection时，如果collection为空会抛异常，所以我们需要在之前经行collection的空值校验*/
        Iterator iterator = collection.iterator();
        Map errors;
        do{
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }

    /**
     * 针对 validate 和 validateList 进行封装，使我们只要只用 validateObject 方法就可以完成校验
     * @param first
     * @param Objects
     * @return
     */
    public static Map<String, String> validateObject (Object first, Object... Objects) {
        if (Objects != null && Objects.length > 0) {
            return new BeanValidator().validateList(Lists.asList(first, Objects));
        } else {
            return new BeanValidator().validate(first, new Class[0]);
        }
    }

    /**
     * 进行参数校验
     * @param param
     * @throws ParamException
     */
    public static void check (Object param) throws ParamException {
        Map<String, String> map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
    }
}
