package eco.ywhc.xr.core.manager;

import com.lark.oapi.Client;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.service.authen.v1.model.CreateOidcAccessTokenReq;
import com.lark.oapi.service.authen.v1.model.CreateOidcAccessTokenReqBody;
import com.lark.oapi.service.authen.v1.model.CreateOidcAccessTokenResp;
import com.lark.oapi.service.authen.v1.model.GetUserInfoResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2ManagerImpl implements OAuth2Manager {

    private final Client larkClient;

    @Override
    @CachePut(cacheNames = "oauth2", key = "#p0")
    public String storeOAuth2State(String state) {
        return OK_FLAG;
    }

    @Override
    @Cacheable(cacheNames = "oauth2", key = "#p0")
    public String retrieveOAuth2State(String state) {
        return "";
    }

    @Override
    public String handleOAuth2Callback(String authorizationCode) {
        CreateOidcAccessTokenReq req = CreateOidcAccessTokenReq.newBuilder()
                .createOidcAccessTokenReqBody(CreateOidcAccessTokenReqBody.newBuilder()
                        .grantType("authorization_code")
                        .code(authorizationCode)
                        .build())
                .build();
        try {
            CreateOidcAccessTokenResp resp = larkClient.authen().oidcAccessToken().create(req);
            if (!resp.success()) {
                log.warn("获取OIDC access token失败");
                return "";
            }
            return resp.getData().getAccessToken();
        } catch (Exception e) {
            log.error("Error creating OIDC access token: {}", e.getMessage());
            throw new InternalErrorException("获取OIDC access token失败");
        }
    }

    @Override
    public String getUserOpenId(String userAccessToken) {
        try {
            RequestOptions requestOptions = RequestOptions.newBuilder()
                    .userAccessToken(userAccessToken)
                    .build();
            GetUserInfoResp resp = larkClient.authen().userInfo().get(requestOptions);
            if (!resp.success()) {
                log.warn("获取用户OpenID失败：{}", resp.getMsg());
                return "";
            }
            return resp.getData().getOpenId();
        } catch (Exception e) {
            log.error("获取用户OpenID失败: {}", e.getMessage());
            throw new InternalErrorException("获取用户OpenID失败");
        }
    }

}
