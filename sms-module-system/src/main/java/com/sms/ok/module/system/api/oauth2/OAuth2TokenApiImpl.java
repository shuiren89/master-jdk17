package com.sms.ok.module.system.api.oauth2;

import com.sms.ok.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import com.sms.ok.framework.common.util.object.BeanUtils;
import com.sms.ok.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import com.sms.ok.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import com.sms.ok.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import com.sms.ok.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.sms.ok.module.system.service.oauth2.OAuth2TokenService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * OAuth2.0 Token API 实现类
 *
 * @author 芋道源码
 */
@Service
public class OAuth2TokenApiImpl implements OAuth2TokenCommonApi {

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Override
    public OAuth2AccessTokenRespDTO createAccessToken(OAuth2AccessTokenCreateReqDTO reqDTO) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                reqDTO.getUserId(), reqDTO.getUserType(), reqDTO.getClientId(), reqDTO.getScopes());
        return BeanUtils.toBean(accessTokenDO, OAuth2AccessTokenRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.checkAccessToken(accessToken);
        return BeanUtils.toBean(accessTokenDO, OAuth2AccessTokenCheckRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenRespDTO removeAccessToken(String accessToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(accessToken);
        return BeanUtils.toBean(accessTokenDO, OAuth2AccessTokenRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenRespDTO refreshAccessToken(String refreshToken, String clientId) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, clientId);
        return BeanUtils.toBean(accessTokenDO, OAuth2AccessTokenRespDTO.class);
    }

}
