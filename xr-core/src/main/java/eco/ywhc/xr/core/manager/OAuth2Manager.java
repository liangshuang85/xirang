package eco.ywhc.xr.core.manager;

/**
 * OAuth2相关处理逻辑
 */
public interface OAuth2Manager {

    String OK_FLAG = "OK";

    /**
     * 缓存{@code state}参数
     *
     * @param state state参数
     */
    String storeOAuth2State(String state);

    /**
     * 获取{@code state}参数对应的值
     *
     * @param state state参数
     */
    String retrieveOAuth2State(String state);

    /**
     * 处理OAuth2回调
     *
     * @param authorizationCode 授权码
     */
    String handleOAuth2Callback(String authorizationCode);

}
