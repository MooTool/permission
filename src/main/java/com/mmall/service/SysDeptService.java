package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysDeptMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.Deptparam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    public void save(Deptparam param){
        BeanValidator.check(param);
        if(chexlExist(param.getParentId(),param.getName(),param.getId())){
            throw  new ParamException(("同一层级下存在相同名称的部门"));
        }
        SysDept sysDept = SysDept.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        sysDept.setOperator(RequestHolder.getCurrentUser().getName());//TODO
        sysDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));//TODO
        sysDept.setOperateTime(new Date());
        sysDeptMapper.insert(sysDept);
    }

    public void update(Deptparam param){
        BeanValidator.check(param);
        if(chexlExist(param.getParentId(),param.getName(),param.getId())){
            throw  new ParamException(("同一层级下存在相同名称的部门"));
        }
      SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的部门不存在");

        if(chexlExist(param.getParentId(),param.getName(),param.getId())){
            throw  new ParamException(("同一层级下存在相同名称的部门"));
        }

        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getName());//TODO
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));//TODO
        after.setOperateTime(new Date());

        updateWithChild(before,after);
    }

    public void delete(int deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
        if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }
    @Transactional
    private void updateWithChild(SysDept before,SysDept after){
        sysDeptMapper.updateByPrimaryKey(after);
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if(!after.getLevel().equals(before.getLevel())){
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if(CollectionUtils.isNotEmpty(deptList)){
                for(SysDept dept : deptList){
                    String level = dept.getLevel();
                    if(level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }


    private boolean chexlExist(Integer parenId,String deptName,Integer deptId){
        return sysDeptMapper.countByNameAndParentId(parenId,deptName,deptId)>0;
    }

    private String getLevel(Integer depId){
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(depId);
        if(sysDept==null){
            return null;
        }else{
           return sysDept.getLevel();
        }
    }
}
