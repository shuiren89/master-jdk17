package com.sms.ok.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Suppliers;
import com.sms.ok.framework.common.biz.system.permission.dto.DeptDataPermissionRespDTO;
import com.sms.ok.framework.common.util.collection.CollectionUtils;
import com.sms.ok.module.system.dal.dataobject.permission.RoleDO;
import com.sms.ok.module.system.enums.permission.DataScopeEnum;
import com.sms.ok.module.system.service.dept.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.sms.ok.framework.common.util.json.JsonUtils.toJsonString;

@Service
@Slf4j
public class DeptDataPermissionService {
    private final DeptService deptService;

    public DeptDataPermissionService(DeptService deptService) {
        this.deptService = deptService;
    }

    public DeptDataPermissionRespDTO calculateDeptDataPermission(Long userId, List<RoleDO> roles, Long userDeptId) {
        DeptDataPermissionRespDTO result = new DeptDataPermissionRespDTO();
        if (CollUtil.isEmpty(roles)) {
            result.setSelf(true);
            return result;
        }

        // 获得用户的部门编号的缓存，通过 Guava 的 Suppliers 惰性求值
        Supplier<Long> userDeptIdSupplier = Suppliers.memoize(() -> userDeptId);

        // 遍历每个角色，计算
        for (RoleDO role : roles) {
            // 为空时，跳过
            if (role.getDataScope() == null) {
                continue;
            }
            // 情况一，ALL
            if (Objects.equals(role.getDataScope(), DataScopeEnum.ALL.getScope())) {
                result.setAll(true);
                continue;
            }
            // 情况二，DEPT_CUSTOM
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_CUSTOM.getScope())) {
                CollUtil.addAll(result.getDeptIds(), role.getDataScopeDeptIds());
                // 自定义可见部门时，保证可以看到自己所在的部门。否则，一些场景下可能会有问题。
                // 例如说，登录时，基于 t_user 的 username 查询会可能被 dept_id 过滤掉
                CollUtil.addAll(result.getDeptIds(), userDeptIdSupplier.get());
                continue;
            }
            // 情况三，DEPT_ONLY
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_ONLY.getScope())) {
                CollectionUtils.addIfNotNull(result.getDeptIds(), userDeptIdSupplier.get());
                continue;
            }
            // 情况四，DEPT_DEPT_AND_CHILD
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_AND_CHILD.getScope())) {
                CollUtil.addAll(result.getDeptIds(), deptService.getChildDeptIdListFromCache(userDeptIdSupplier.get()));
                // 添加本身部门编号
                CollUtil.addAll(result.getDeptIds(), userDeptIdSupplier.get());
                continue;
            }
            // 情况五，SELF
            if (Objects.equals(role.getDataScope(), DataScopeEnum.SELF.getScope())) {
                result.setSelf(true);
                continue;
            }
            // 未知情况，error log 即可
            log.error("[calculateDeptDataPermission][LoginUser({}) role({}) 无法处理]", userId, toJsonString(result));
        }
        return result;
    }
}
