package com.sms.ok.module.system.service.dict;

import cn.hutool.core.util.StrUtil;
import com.sms.ok.framework.common.enums.CommonStatusEnum;
import com.sms.ok.module.system.dal.dataobject.dict.DictDataDO;
import com.sms.ok.module.system.dal.dataobject.dict.DictTypeDO;
import com.sms.ok.module.system.dal.mysql.dict.DictDataMapper;
import com.sms.ok.module.system.dal.mysql.dict.DictTypeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.sms.ok.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sms.ok.module.system.enums.ErrorCodeConstants.*;

@Service
public class DictValidationService {

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Resource
    private DictDataMapper dictDataMapper;

    /**
     * 验证字典类型是否存在且启用
     */
    public void validateDictTypeExists(String type) {
        if (StrUtil.isEmpty(type)) {
            return;
        }
        DictTypeDO dictType = dictTypeMapper.selectByType(type);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dictType.getStatus())) {
            throw exception(DICT_TYPE_NOT_ENABLE);
        }
    }

    /**
     * 验证字典类型名称是否唯一
     */
    public void validateDictTypeNameUnique(Long id, String name) {
        DictTypeDO dictType = dictTypeMapper.selectByName(name);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
    }

    /**
     * 验证字典类型编码是否唯一
     */
    public void validateDictTypeUnique(Long id, String type) {
        if (StrUtil.isEmpty(type)) {
            return;
        }
        DictTypeDO dictType = dictTypeMapper.selectByType(type);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
    }

    /**
     * 验证字典数据值是否唯一
     */
    public void validateDictDataValueUnique(Long id, String dictType, String value) {
        DictDataDO dictData = dictDataMapper.selectByDictTypeAndValue(dictType, value);
        if (dictData == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
        if (id == null) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
        if (!dictData.getId().equals(id)) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
    }

    /**
     * 验证字典数据列表是否有效
     */
    public void validateDictDataList(String dictType, Collection<String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }

        // 验证字典类型是否存在且启用
        validateDictTypeExists(dictType);

        // 验证每个字典数据值
        for (String value : values) {
            DictDataDO dictData = dictDataMapper.selectByDictTypeAndValue(dictType, value);
            if (dictData == null) {
                throw exception(DICT_DATA_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dictData.getStatus())) {
                throw exception(DICT_DATA_NOT_ENABLE, dictData.getLabel());
            }
        }
    }

    /**
     * 获取指定字典类型的数据数量
     */
    public long getDictDataCountByDictType(String dictType) {
        return dictDataMapper.selectCountByDictType(dictType);
    }

    /**
     * 验证字典类型是否存在
     */
    public DictTypeDO validateDictTypeExists(Long id) {
        if (id == null) {
            return null;
        }
        DictTypeDO dictType = dictTypeMapper.selectById(id);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        return dictType;
    }

    /**
     * 验证字典数据是否存在
     */
    public DictDataDO validateDictDataExists(Long id) {
        if (id == null) {
            return null;
        }
        DictDataDO dictData = dictDataMapper.selectById(id);
        if (dictData == null) {
            throw exception(DICT_DATA_NOT_EXISTS);
        }
        return dictData;
    }
}
