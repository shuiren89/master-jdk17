package com.sms.ok.module.system.service.permission.impl;

import com.sms.ok.framework.common.biz.system.permission.PermissionCommonApi;
import com.sms.ok.framework.common.biz.system.permission.dto.DeptDataPermissionRespDTO;
import com.sms.ok.module.system.service.permission.PermissionService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PermissionCommonApiImpl implements PermissionCommonApi {

    private final PermissionService permissionService;

    public PermissionCommonApiImpl(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean hasPermission(Long userId, String permission) {
        return permissionService.hasAnyPermissions(userId, permission);
    }

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return permissionService.hasAnyPermissions(userId, permissions);
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        return permissionService.hasAnyRoles(userId, roles);
    }

    @Override
    public DeptDataPermissionRespDTO getDeptDataPermission(Long userId) {
        return null;
    }

}
