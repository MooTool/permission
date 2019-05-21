package com.mmall.controller;

import com.mmall.common.ApplicationContextHelper;
import com.mmall.common.JsonData;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.exception.PermissionException;
import com.mmall.model.SysAclModule;
import com.mmall.param.TestVo;
import com.mmall.util.BeanValidator;
import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
        throw new RuntimeException("test exception");
        // return JsonData.success("hello, permission");
       // return JsonData.success("hello,json");
    }

  @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo){
        log.info("validate");
        /*try{
            Map<String,String> map = BeanValidator.validateObject(vo);
           // if(map != null && map.entrySet().size()>0){
            if(MapUtils.isNotEmpty(map)){
                for (Map.Entry<String,String> entry:map.entrySet()){
                    log.info("{}->{}",entry.getKey(),entry.getValue());
                }
            }
        }catch(Exception e){

        }*/
        return JsonData.success("test validate");
    }
    @RequestMapping("/validateM.json")
    @ResponseBody
    public JsonData validateMapper(TestVo vo){
        log.info("validateM");
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
      SysAclModule module=   moduleMapper.selectByPrimaryKey(1);
      log.info(JsonMapper.obj2String(module));
        BeanValidator.check(vo);
        return JsonData.success("test validate");
    }
}
