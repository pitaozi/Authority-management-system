package com.taoyuan.service;

import com.google.common.base.Preconditions;
import com.taoyuan.beans.PageQuery;
import com.taoyuan.beans.PageResult;
import com.taoyuan.common.RequestHolder;
import com.taoyuan.dao.SysUserMapper;
import com.taoyuan.exception.ParamException;
import com.taoyuan.model.SysUser;
import com.taoyuan.param.UserParam;
import com.taoyuan.utils.BeanValidator;
import com.taoyuan.utils.IpUtil;
import com.taoyuan.utils.MD5Util;
import com.taoyuan.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName permission
 * @ClassName SysUserService
 * @Date 2019/12/17 17:18
 * @Author taoyuan
 * @Version 1.0
 */
@Service
@Slf4j
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    public void saveUser (UserParam userParam) {
        BeanValidator.check(userParam);
        if (checkTelephoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(userParam.getMail(), userParam.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        String password = PasswordUtil.randomPassword();
        // TODO:
        password = "12345678";
        // MD5加密的密码
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser sysUser = SysUser.builder()
                .username(userParam.getUsername())
                .telephone(userParam.getTelephone())
                .mail(userParam.getMail())
                .password(encryptedPassword)
                .deptId(userParam.getDeptId())
                .status(userParam.getStatus())
                .remark(userParam.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysUser.setOperateTime(new Date());

        // TODO: sendEmail

        sysUserMapper.insert(sysUser);
        sysLogService.saveUserLog(null, sysUser);
    }

    /**
     * 更新用户信息接口
     * @param userParam
     */
    public void updateUser (UserParam userParam) {
        BeanValidator.check(userParam);
        if (checkTelephoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(userParam.getMail(), userParam.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        SysUser after = SysUser.builder()
                .id(userParam.getId())
                .username(userParam.getUsername())
                .telephone(userParam.getTelephone())
                .mail(userParam.getMail())
                .password(before.getPassword())
                .deptId(userParam.getDeptId())
                .status(userParam.getStatus())
                .remark(userParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKey(after);
        sysLogService.saveUserLog(before, after);
    }
    public boolean checkEmailExist (String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    public boolean checkTelephoneExist (String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }

    public SysUser findByKeyword (String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }

    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page) {
        BeanValidator.check(page);
        // 查询分页总数
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }
}
