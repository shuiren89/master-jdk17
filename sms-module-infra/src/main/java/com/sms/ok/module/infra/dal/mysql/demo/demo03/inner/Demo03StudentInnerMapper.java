package com.sms.ok.module.infra.dal.mysql.demo.demo03.inner;

import com.sms.ok.framework.common.pojo.PageResult;
import com.sms.ok.framework.mybatis.core.mapper.BaseMapperX;
import com.sms.ok.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sms.ok.module.infra.controller.admin.demo.demo03.inner.vo.Demo03StudentInnerPageReqVO;
import com.sms.ok.module.infra.dal.dataobject.demo.demo03.Demo03StudentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface Demo03StudentInnerMapper extends BaseMapperX<Demo03StudentDO> {

    default PageResult<Demo03StudentDO> selectPage(Demo03StudentInnerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<Demo03StudentDO>()
                .likeIfPresent(Demo03StudentDO::getName, reqVO.getName())
                .eqIfPresent(Demo03StudentDO::getSex, reqVO.getSex())
                .eqIfPresent(Demo03StudentDO::getDescription, reqVO.getDescription())
                .betweenIfPresent(Demo03StudentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(Demo03StudentDO::getId));
    }

}