package com.taoyuan.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.taoyuan.common.RequestHolder;
import com.taoyuan.dao.SysRoleAclMapper;
import com.taoyuan.dao.SysRoleMapper;
import com.taoyuan.dao.SysRoleUserMapper;
import com.taoyuan.dao.SysUserMapper;
import com.taoyuan.exception.ParamException;
import com.taoyuan.model.SysRole;
import com.taoyuan.model.SysUser;
import com.taoyuan.param.RoleParam;
import com.taoyuan.utils.BeanValidator;
import com.taoyuan.utils.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName permission
 * @ClassName SysRoleService
 * @Date 2019/12/19 9:40
 * @Author taoyuan
 * @Version 1.0
 */
@Service
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    public void save(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        SysRole role = SysRole.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());
        sysRoleMapper.insert(role);
        sysLogService.saveRoleLog(null, role);
    }

    public void update(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKey(after);
        sysLogService.saveRoleLog(before, after);
    }

    public void delete(Integer roleId) {
        SysRole before = sysRoleMapper.selectByPrimaryKey(roleId);
        Preconditions.checkNotNull(before, "待删除的用户不存在，无法删除");
        //角色下面有权限，不能删除
        if (sysRoleAclMapper.countByRoleId(roleId) > 0) {
            throw new ParamException("当前角色下面有权限点，无法删除");
        }
        //角色下面有用户，不能删除
        if (sysRoleUserMapper.countByRoleId(roleId) > 0) {
            throw new ParamException("当前角色下面有用户，无法删除");
        }
        sysRoleMapper.deleteByPrimaryKey(roleId);
        sysLogService.saveRoleLog(before, null);
    }

    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }

    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        } else {
            return sysRoleMapper.getByIdList(roleIdList);
        }
    }

    public List<SysRole> getRoleListByAclId(int aclId) {
        List<Integer> userIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        } else {
            return sysRoleMapper.getByIdList(userIdList);
        }
    }

    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        //如果这些角色都没有用户，空返回
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        //拿到对应用户
        return sysUserMapper.getByIdList(userIdList);
    }
}
