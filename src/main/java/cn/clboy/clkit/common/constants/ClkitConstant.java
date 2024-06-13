package cn.clboy.clkit.common.constants;

/**
 * 应用程序常量
 *
 * @author clboy
 * @date 2024/05/17 15:31:21
 */
public interface ClkitConstant {

    /**
     * 认证API URL
     */
    String AUTH_API_URL = "/api/auth/token";

    /**
     * 退出登录API URL
     */
    String LOGOUT_API_URL = "/api/auth/logout";

    /**
     * 存储请求token的key
     */
    String TOKEN_ATTRIBUTE_KEY = "__clkit_token";

    /**
     * 存储用户信息的key
     */
    String USER_ATTRIBUTE_KEY = "__clkit_user";

    /**
     * 用户ID列名
     */
    String USER_ID_COLUMN_NAME = "user_id";

    /**
     * 用户ID字段名称
     */
    String USER_ID_FIELD_NAME = "userId";

    /**
     * hibernate用户数据过滤器名称
     */
    String HIBERNATE_USER_DATA_FILTER_NAME = "userDataFilter";

    /**
     * 角色编码前缀
     */
    String ROLE_CODE_PREFIX = "ROLE_";

    /**
     * websocket端点路径
     */
    String WEBSOCKET_ENDPOINT_PATH = "/api/websocket";
}
