package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.MD5Util;
import com.mmall.util.PasswordUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    public void save(UserParam param){
        BeanValidator.check(param);
        if(checkTelpExist(param.getTelephone(),param.getId())){
            throw new ParamException("电话已被占用");
        }

        if(checkEmailExist(param.getMail(),param.getId())){
            throw new ParamException("邮箱已被占用");
        }
        String password = PasswordUtil.randomPassword();
         password = "123456";//通过邮件通知
        String encrytedPassword = MD5Util.encrypt(password);
        SysUser sysUser =SysUser.builder().name(param.getName()).telephone(param.getTelephone())
        .mail(param.getMail()).password(encrytedPassword).deptId(param.getDeptId()).status(param.getStatus())
                .remark(param.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentUser().getName());//TODO
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));//TODO
        sysUser.setOperateTime(new Date());
        //TODO 发送email成功之后 通知
        sysUserMapper.insertSelective(sysUser);

    }

    public void update(UserParam param){
        BeanValidator.check(param);
        if(checkTelpExist(param.getTelephone(),param.getId())){
            throw new ParamException("电话已被占用");
        }

        if(checkEmailExist(param.getMail(),param.getId())){
            throw new ParamException("邮箱已被占用");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的用户不存在");
        SysUser after = SysUser.builder().id(param.getId()).name(param.getName()).telephone(param.getTelephone())
                .mail(param.getMail()).password(before.getPassword()).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark())
                .build();
        after.setOperator(RequestHolder.getCurrentUser().getName());//TODO
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));//TODO
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);

    }

    public boolean checkEmailExist(String mail,Integer userId){

        return sysUserMapper.countByMail(mail,userId)>0;
    }

    public boolean checkTelpExist(String telephone,Integer userId){
        return sysUserMapper.countByMail(telephone,userId)>0;
    }

    public SysUser findByKeyWord(String keyword){
        return sysUserMapper.findByKeyWord(keyword);
    }

    public PageResult<SysUser>  getPageByDeptId(int deptId, PageQuery pageQuery){
        BeanValidator.check(pageQuery);
        int count = sysUserMapper.countByDeptId(deptId);
        if(count>0){
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId,pageQuery);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();

    }

    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }
}
