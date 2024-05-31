package cn.clboy.clkit.common.constants;

/**
 * 权限常量
 * 权限名称规范，一级分类名-n级分类名-名称
 *
 * @author clboy
 * @date 2024/05/20 14:19:27
 */
public interface PermissionConstant {

    /**
     * 管理员
     */
    String ADMIN_ROLE_CODE = "ROLE_ADMIN";

    /**
     * 系统-数据备份-导出
     */
    String APP_EXPORT = "app_export";

    /**
     * 系统-数据备份-导入
     */
    String APP_IMPORT = "app_import";

    /**
     * 扩展-管理
     */
    String EXTENSION_MANAGE = "extension_manage";

    /**
     * 代码生成-增删改查
     */
    String GEN_CRUD = "gen_crud";

    /**
     * 代码生成-增删改查-模板管理
     */
    String GEN_CURD_TEMPLATE_MANAGE = "gen_curd_template_manage";

    /**
     * 代码生成-增删改查-查看
     */
    String GEN_CURD_TEMPLATE_VIEW = "gen_curd_template_view";

    /**
     * 代码生成-增删改查-类型映射管理
     */
    String GEN_DB_LANG_TYPE_MANAGE = "gen_db_lang_type_manage";

    /**
     * 代码生成-增删改查-类型映射查看
     */
    String GEN_DB_LANG_TYPE_VIEW = "gen_db_lang_type_view";

    /**
     * 代码生成-增删改查-数据库管理
     */
    String GEN_DB_MANAGE = "gen_db_manage";

    /**
     * 代码生成-增删改查-数据库查看
     */
    String GEN_DB_VIEW = "gen_db_view";

    /**
     * 任务-待办-管理
     */
    String JOB_TODO_MANAGE = "job_todo_manage";

    /**
     * 任务-待办-查看
     */
    String JOB_TODO_VIEW = "job_todo_view";

    /**
     * 网络-局域网-查看
     */
    String NET_LAN_VIEW = "net_lan_view";

    /**
     * 操作系统-进程-查看
     */
    String OS_PROCESS_VIEW = "os_process_view";

    /**
     * 系统-用户-查看
     */
    String UPMS_USER_VIEW = "upms_user_view";

    /**
     * 系统-用户-管理
     */
    String UPMS_USER_MANAGE = "upms_user_manage";

    /**
     * 系统-用户-重置密码
     */
    String UPMS_USER_RESET_PASSWORD = "upms_user_reset_password";

    /**
     * 系统-角色-查看
     */
    String UPMS_ROLE_VIEW = "upms_role_view";

    /**
     * 系统-角色-管理
     */
    String UPMS_ROLE_MANAGE = "upms_role_manage";

    /**
     * 系统-权限-查看
     */
    String UPMS_PERMISSION_VIEW = "upms_permission_view";
}
