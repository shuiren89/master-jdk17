package com.sms.ok.module.system.service.dict;

import cn.hutool.core.collection.CollUtil;
import com.sms.ok.framework.common.enums.CommonStatusEnum;
import com.sms.ok.framework.common.pojo.PageResult;
import com.sms.ok.framework.common.util.collection.CollectionUtils;
import com.sms.ok.framework.common.util.object.BeanUtils;
import com.sms.ok.module.system.controller.admin.dict.vo.data.DictDataPageReqVO;
import com.sms.ok.module.system.controller.admin.dict.vo.data.DictDataSaveReqVO;
import com.sms.ok.module.system.dal.dataobject.dict.DictDataDO;
import com.sms.ok.module.system.dal.dataobject.dict.DictTypeDO;
import com.sms.ok.module.system.dal.mysql.dict.DictDataMapper;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.sms.ok.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sms.ok.module.system.enums.ErrorCodeConstants.*;

/**
 * 字典数据 Service 实现类
 *
 * @author ruoyi
 */
@Service
@Slf4j
public class DictDataServiceImpl implements DictDataService {

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<DictDataDO> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(DictDataDO::getDictType)
            .thenComparingInt(DictDataDO::getSort);

    @Resource
    private DictValidationService dictValidationService;

    @Resource
    private DictDataMapper dictDataMapper;

    @Override
    public List<DictDataDO> getDictDataList(Integer status, String dictType) {
        List<DictDataDO> list = dictDataMapper.selectListByStatusAndDictType(status, dictType);
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    @Override
    public PageResult<DictDataDO> getDictDataPage(DictDataPageReqVO pageReqVO) {
        return dictDataMapper.selectPage(pageReqVO);
    }

    @Override
    public DictDataDO getDictData(Long id) {
        return dictDataMapper.selectById(id);
    }

    @Override
    public Long createDictData(DictDataSaveReqVO createReqVO) {
        // 校验字典类型有效
        dictValidationService.validateDictTypeExists(createReqVO.getDictType());
        // 校验字典数据的值的唯一性
        dictValidationService.validateDictDataValueUnique(null, createReqVO.getDictType(), createReqVO.getValue());

        // 插入字典类型
        DictDataDO dictData = BeanUtils.toBean(createReqVO, DictDataDO.class);
        dictDataMapper.insert(dictData);
        return dictData.getId();
    }

    @Override
    public void updateDictData(DictDataSaveReqVO updateReqVO) {
        // 校验自己存在
        dictValidationService.validateDictDataExists(updateReqVO.getId());
        // 校验字典类型有效
        dictValidationService.validateDictTypeExists(updateReqVO.getDictType());
        // 校验字典数据的值的唯一性
        dictValidationService.validateDictDataValueUnique(updateReqVO.getId(), updateReqVO.getDictType(), updateReqVO.getValue());

        // 更新字典类型
        DictDataDO updateObj = BeanUtils.toBean(updateReqVO, DictDataDO.class);
        dictDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictData(Long id) {
        // 校验是否存在
        dictValidationService.validateDictDataExists(id);

        // 删除字典数据
        dictDataMapper.deleteById(id);
    }

    @Override
    public void deleteDictDataList(List<Long> ids) {
        dictDataMapper.deleteByIds(ids);
    }

    @Override
    public long getDictDataCountByDictType(String dictType) {
        return dictDataMapper.selectCountByDictType(dictType);
    }

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        dictValidationService.validateDictDataList(dictType, values);
    }

    @Override
    public DictDataDO getDictData(String dictType, String value) {
        return dictDataMapper.selectByDictTypeAndValue(dictType, value);
    }

    @Override
    public DictDataDO parseDictData(String dictType, String label) {
        return dictDataMapper.selectByDictTypeAndLabel(dictType, label);
    }

    @Override
    public List<DictDataDO> getDictDataListByDictType(String dictType) {
        List<DictDataDO> list = dictDataMapper.selectList(DictDataDO::getDictType, dictType);
        list.sort(Comparator.comparing(DictDataDO::getSort));
        return list;
    }
}
