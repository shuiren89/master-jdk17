package com.sms.ok.module.system.service.dict;

import cn.hutool.core.util.StrUtil;
import com.sms.ok.framework.common.pojo.PageResult;
import com.sms.ok.framework.common.util.date.LocalDateTimeUtils;
import com.sms.ok.framework.common.util.object.BeanUtils;
import com.sms.ok.module.system.controller.admin.dict.vo.type.DictTypePageReqVO;
import com.sms.ok.module.system.controller.admin.dict.vo.type.DictTypeSaveReqVO;
import com.sms.ok.module.system.dal.dataobject.dict.DictTypeDO;
import com.sms.ok.module.system.dal.mysql.dict.DictTypeMapper;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.sms.ok.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sms.ok.module.system.enums.ErrorCodeConstants.*;

/**
 * 字典类型 Service 实现类
 */
@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Resource
    private DictValidationService dictValidationService;

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Override
    public PageResult<DictTypeDO> getDictTypePage(DictTypePageReqVO pageReqVO) {
        return dictTypeMapper.selectPage(pageReqVO);
    }

    @Override
    public DictTypeDO getDictType(Long id) {
        return dictTypeMapper.selectById(id);
    }

    @Override
    public DictTypeDO getDictType(String type) {
        return dictTypeMapper.selectByType(type);
    }

    @Override
    public Long createDictType(DictTypeSaveReqVO createReqVO) {
        // 校验字典类型的名字的唯一性
        dictValidationService.validateDictTypeNameUnique(null, createReqVO.getName());
        // 校验字典类型的类型的唯一性
        dictValidationService.validateDictTypeUnique(null, createReqVO.getType());

        // 插入字典类型
        DictTypeDO dictType = BeanUtils.toBean(createReqVO, DictTypeDO.class);
        dictType.setDeletedTime(LocalDateTimeUtils.EMPTY); // 唯一索引，避免 null 值
        dictTypeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void updateDictType(DictTypeSaveReqVO updateReqVO) {
        // 校验自己存在
        dictValidationService.validateDictTypeExists(updateReqVO.getId());
        // 校验字典类型的名字的唯一性
        dictValidationService.validateDictTypeNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 校验字典类型的类型的唯一性
        dictValidationService.validateDictTypeUnique(updateReqVO.getId(), updateReqVO.getType());

        // 更新字典类型
        DictTypeDO updateObj = BeanUtils.toBean(updateReqVO, DictTypeDO.class);
        dictTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictType(Long id) {
        // 校验是否存在
        DictTypeDO dictType = dictValidationService.validateDictTypeExists(id);
        // 校验是否有字典数据
        if (dictValidationService.getDictDataCountByDictType(dictType.getType()) > 0) {
            throw exception(DICT_TYPE_HAS_CHILDREN);
        }
        // 删除字典类型
        dictTypeMapper.updateToDelete(id, LocalDateTime.now());
    }

    @Override
    public void deleteDictTypeList(List<Long> ids) {
        // 1. 校验是否有字典数据
        List<DictTypeDO> dictTypes = dictTypeMapper.selectByIds(ids);
        dictTypes.forEach(dictType -> {
            if (dictValidationService.getDictDataCountByDictType(dictType.getType()) > 0) {
                throw exception(DICT_TYPE_HAS_CHILDREN);
            }
        });

        // 2. 批量删除字典类型
        LocalDateTime now = LocalDateTime.now();
        ids.forEach(id -> dictTypeMapper.updateToDelete(id, now));
    }

    @Override
    public List<DictTypeDO> getDictTypeList() {
        return dictTypeMapper.selectList();
    }
}
