package cn.clboy.clkit;

import cn.clboy.clkit.app.entity.ClkitProperty;
import cn.clboy.clkit.app.service.ClkitPropertyService;
import cn.clboy.clkit.common.component.jpa.BaseRepositoryImpl;
import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.common.util.AppUtils;
import cn.clboy.clkit.config.AppProperties;
import cn.clboy.clkit.os.handler.OsHandler;
import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * clkit 应用程序
 *
 * @author clboy
 * @date 2024/04/18 11:26:21
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class ClkitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClkitApplication.class, args);
        initData(context);
        printAppInfo();
        openOnStartup(context);
    }

    /**
     * 初始化数据
     *
     * @param context 上下文
     */
    private static void initData(ConfigurableApplicationContext context) {
        try {
            //视为已由自动配置初始化
            context.getBean(SqlDataSourceScriptDatabaseInitializer.class);
            return;
        } catch (NoSuchBeanDefinitionException e) {
            //ignore
        }

        ClkitPropertyService propertyService = context.getBean(ClkitPropertyService.class);
        ClkitProperty property = propertyService.getByKey(ClkitConstant.DATA_INITIALIZED_KEY);
        if (property == null) {
            property = new ClkitProperty();
            property.setName("程序数据已初始化");
            property.setPropKey(ClkitConstant.DATA_INITIALIZED_KEY);
            property.setPropValue(Boolean.FALSE.toString());
            property.setInternal(Boolean.TRUE);
        }
        if (Boolean.TRUE.toString().equals(property.getPropValue())) {
            return;
        }
        DataSource dataSource = context.getBean(DataSource.class);
        SqlInitializationProperties initializationProperties = new SqlInitializationProperties();
        initializationProperties.setMode(DatabaseInitializationMode.ALWAYS);
        SqlDataSourceScriptDatabaseInitializer initializer = new SqlDataSourceScriptDatabaseInitializer(dataSource, initializationProperties);
        initializer.setResourceLoader(context);
        initializer.initializeDatabase();
        property.setPropValue(Boolean.TRUE.toString());
        propertyService.save(property);
    }

    /**
     * 打印应用程序信息
     */
    private static void printAppInfo() {
        log.info("应用程序主目录：{}", AppUtils.getHomeDirPath());
        log.info("应用程序数据目录：{}", AppUtils.getDataDirPath());
    }

    /**
     * 启动时打开
     *
     * @param context 上下文
     */
    private static void openOnStartup(ApplicationContext context) {
        ServerProperties serverProperties = context.getBean(ServerProperties.class);

        StringBuilder urlBuilder = new StringBuilder("http://");
        if (serverProperties.getAddress() == null) {
            urlBuilder.append(NetUtil.LOCAL_IP);
        } else {
            urlBuilder.append(serverProperties.getAddress().getHostAddress());
        }
        urlBuilder.append(":").append(serverProperties.getPort());
        if (StringUtils.hasText(serverProperties.getServlet().getContextPath())) {
            urlBuilder.append("/").append(serverProperties.getServlet().getContextPath());
        }

        String url = urlBuilder.toString();
        log.info("服务器已启动，访问路径：{}", url);

        AppProperties appProperties = context.getBean(AppProperties.class);
        if (Boolean.TRUE.equals(appProperties.getOpenOnStartup())) {
            OsHandler osHandler = context.getBean(OsHandler.class);
            try {
                osHandler.openUrl(url);
            } catch (Exception ex) {
                log.error("启动浏览器失败", ex);
            }
        }

    }

}
