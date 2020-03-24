package com.taoyuan.service;

import com.google.common.base.Preconditions;
import com.taoyuan.common.RequestHolder;
import com.taoyuan.dao.SysDeptMapper;
import com.taoyuan.dao.SysUserMapper;
import com.taoyuan.exception.ParamException;
import com.taoyuan.model.SysDept;
import com.taoyuan.param.DeptParam;
import com.taoyuan.utils.BeanValidator;
import com.taoyuan.utils.DeptLevelUtil;
import com.taoyuan.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName permission
 * @ClassName SysDeptService
 * @Date 2019/12/13 13:52
 * @Author taoyuan
 * @Version 1.0
 */
@Service
@Slf4j
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    public void saveDept (DeptParam deptParam) {
        BeanValidator.validateObject(deptParam);
        if (checkDeptExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下具有相同名称的部门");
        }
        SysDept sysDept = SysDept.builder()
                .name(deptParam.getName())
                .parentId(deptParam.getParentId())
                .seq(deptParam.getSeq())
                .remark(deptParam.getRemark()).build();
        sysDept.setLevel(DeptLevelUtil.calculateLevel(
                getDeptLevel(deptParam.getParentId()), deptParam.getParentId()));
        sysDept.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysDept.setOperateTime(new Date());
        sysDeptMapper.insert(sysDept);// insertSelective 会忽视校验
        sysLogService.saveDeptLog(null, sysDept);
    }

    private boolean checkDeptExist (Integer parentId, String deptName, Integer deptId) {
        // 部门是否重复校验
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    private String getDeptLevel (Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            return null;
        }
        return sysDept.getLevel();
    }

    /**
     * 更新部门信息接口
     * @param deptParam
     */
    public void updateDept (DeptParam deptParam) {
        BeanValidator.check(deptParam);
        if (checkDeptExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下有相同名称的部门");
        }
        // 获取之前的部门信息
        SysDept before = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        // 校验之前的部门信息是否为空，为空就抛出异常
        Preconditions.checkNotNull(before, "待更新的部门不存在");
        if (checkDeptExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下具有相同名称的部门");
        }
        SysDept after = SysDept.builder()
                .id(deptParam.getId())
                .name(deptParam.getName())
                .parentId(deptParam.getParentId())
                .seq(deptParam.getSeq())
                .remark(deptParam.getRemark()).build();
        after.setLevel(DeptLevelUtil.calculateLevel(getDeptLevel(deptParam.getParentId()), deptParam.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        // 修改完当前部门，修改子部门信息
        updateDeptWithChild(before, after);
        sysLogService.saveDeptLog(before, after);
    }

    /**
     * 更新子部门数据，要求将其放置在Sptring事务中，要么都成功，要么就都失败
     * @param before
     * @param after
     */
    @Transactional
    public void updateDeptWithChild (SysDept before, SysDept after) {
        // 获取新的部门层级信息
        String newLevelPrefix = after.getLevel();
        // 获取旧的部门层级信息
        String oldLevelPrefix = before.getLevel();

        // 判断新旧部门层级信息是否一致，如果一致不需要修改
        // 不一致的情况下才需要修改部门信息
        if (!newLevelPrefix.equals(oldLevelPrefix)) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(oldLevelPrefix);
            // 只有当我们deptList不为空的情况下，才会去修改我们的子部门信息
            if (CollectionUtils.isNotEmpty(deptList)) {
                // 遍历deptList获取子部门信息
                for (SysDept dept : deptList) {
                    // 获取子部门信息的level
                    String level = dept.getLevel();
                    // 我们当前子部门层级信息内包含原有部门层级信息前缀才进行修改
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix.concat(level.substring(oldLevelPrefix.length()));
                        // 设置部门信息
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        // 修改子部门信息
        sysDeptMapper.updateByPrimaryKey(after);
    }

    /**
     * 删除部门信息接口
     * @param deptId
     */
    public void deleteDept (Integer deptId) {
        SysDept before = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(before, "待删除的部门不存在，无法删除");
        if (sysDeptMapper.countByParentId(deptId) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
        if (sysUserMapper.countByDeptId(deptId) > 0) {
            throw new ParamException("当前部门下面有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
        sysLogService.saveDeptLog(before, null);
    }
}
