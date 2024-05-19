package cn.clboy.clkit.common.constants;

/**
 * 应用程序常量
 *
 * @author clboy
 * @date 2024/05/17 15:31:21
 */
public interface ClkitConstant {

    /**
     * 数据初始化key
     */
    String DATA_INITIALIZED_KEY = "CLKIT_DATA_INITIALIZED";

    /**
     * 管理员角色代码
     */
    String ADMIN_ROLE_CODE = "ROLE_ADMIN";

    /**
     * 认证API URL
     */
    String AUTH_API_URL = "/api/auth/token";

    /**
     * 退出登录API URL
     */
    String LOGOUT_API_URL = "/api/auth/logout";
}
