package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAcl;
import com.mmall.param.AclParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Created by wcc on 2019\5\21 0021 22:25
 */
@Service
public class SysAclService {
    @Resource
    private SysAclMapper sysAclMapper;

    public void save(AclParam param){
        BeanValidator.check(param);
        if(checkExist(param.getAclModuleId(),param.getName(),param.getId())){
            throw new ParamException("当前模块下存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId())
                .url(param.getUrl()).type(param.getType()).status(param.getStatus())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setCode(generatorCode());
        acl.setOperator(RequestHolder.getCurrentUser().getName());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        acl.setOperateTime(new Date());
        sysAclMapper.insertSelective(acl);
    }

    public void update(AclParam param){
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的权限点不存在");

        SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId())
                .url(param.getUrl()).type(param.getType()).status(param.getStatus())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        sysAclMapper.updateByPrimaryKeySelective(after);
    }

    public boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    public String generatorCode(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date())+ "_" + (int)(Math.random() * 100);
    }

    //分页数据

    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery page){
        BeanValidator.check(page);
        int count= sysAclMapper.countByAclModuleId(aclModuleId);
        if(count>0){
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId,page);
            return PageResult.<SysAcl>builder().data(aclList).total(count).build();
        }
        return PageResult.<SysAcl>builder().build();
    }
}
