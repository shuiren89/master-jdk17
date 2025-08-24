package com.sms.ok.module.system.service.permission.impl;

import com.sms.ok.module.system.dal.dataobject.user.AdminUserDO;
import com.sms.ok.module.system.service.permission.UserDeptInfoService;
import com.sms.ok.module.system.service.user.AdminUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserDeptInfoServiceImpl implements UserDeptInfoService {

    private final AdminUserService adminUserService;

    public UserDeptInfoServiceImpl(@Lazy AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Override
    public Long getUserDeptId(Long userId) {
        AdminUserDO user = adminUserService.getUser(userId);
        return user != null ? user.getDeptId() : null;
    }

    @Override
    public AdminUserDO getUserInfo(Long userId) {
        return adminUserService.getUser(userId);
    }
}