package cn.clboy.clkit;

import cn.clboy.clkit.config.AppProperties;
import cn.clboy.clkit.os.handler.OsHandler;
import cn.clboy.clkit.util.AppUtils;
import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * clkit 应用程序
 *
 * @author clboy
 * @date 2024/04/18 11:26:21
 */
@Slf4j
@SpringBootApplication
public class ClkitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClkitApplication.class, args);
        printAppInfo();
        openOnStartup(context);
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
            osHandler.openUrl(url);
        }

    }


}
