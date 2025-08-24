package com.sms.ok.module.system.service.permission;

import com.sms.ok.module.system.dal.dataobject.user.AdminUserDO;

public interface UserDeptInfoService {
    Long getUserDeptId(Long userId);
    AdminUserDO getUserInfo(Long userId);
}
