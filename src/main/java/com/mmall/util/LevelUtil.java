package com.mmall.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 计算级别
 */
public class LevelUtil {
    //默认使用.分割
    public final static String  SEPARATOR = ".";
    //首层默认为0
    public final  static  String ROOT = "0";
    public static  String  calculateLevel(String parentLevel,int parentId){
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }else{
            return StringUtils.join(parentLevel,SEPARATOR,parentId);
        }
    }
}
