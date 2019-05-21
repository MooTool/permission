package com.mmall.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 校验工具
 * 基于注解 比如@NotBlank(String) 不能为空的字段  @NotNull 不能为null的字段 @NotEmpty(对象-list等)
 */
public class BeanValidator {

    private  static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    public static <T> Map<String,String> validate(T t, Class... groups){
        Validator validator = validatorFactory.getValidator();
        Set validateValue = validator.validate(t,groups);
        if(validateValue.isEmpty()){
            return Collections.emptyMap();
        }else{
            LinkedHashMap errors = Maps.newLinkedHashMap();//谷歌里的方法
            Iterator iterator = validateValue.iterator();
            while (iterator.hasNext()){
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(),violation.getMessage());
            }
            return errors;
        }
    }

    public static  Map<String,String> validateList(Collection<?> collection){
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;
        do{
            if(!iterator.hasNext()){
                return  Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object,new Class[0]);
        }while(errors.isEmpty());
            return errors;
    }

    public static Map<String,String> validateObject(Object first,Object... objects){
        if(objects != null && objects.length>0){
           return validateList(Lists.asList(first,objects));
        }else{
            return validate(first,new Class[0]);
        }
    }

    public static void check(Object param) throws ParamException{
        Map<String,String> map = BeanValidator.validateObject(param);
       // if(map!=null && map.entrySet().size()>0){
        if(MapUtils.isNotEmpty(map)){
            throw  new ParamException(map.toString());
        }
    }
}
