package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by wcc on 2019\5\21 0021 16:49
 */
@Getter
@Setter
@ToString
public class AclModuleLevelDto extends SysAclModule {
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    private List<AclDto> aclList = Lists.newArrayList();

    public static AclModuleLevelDto adapt(SysAclModule aclModule){
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule,dto);
        return dto;
    }
}
