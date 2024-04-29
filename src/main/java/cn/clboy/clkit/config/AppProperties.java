package cn.clboy.clkit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 应用程序属性
 *
 * @author clboy
 * @date 2024/04/18 14:27:45
 */
@Data
@ConfigurationProperties(prefix = "clkit")
public class AppProperties {

    public static final String DEFAULT_DATA_DIR_PATH = "~/.clkit";

    /**
     * 数据目录路径
     */
    private String dataDirPath = DEFAULT_DATA_DIR_PATH;

    /**
     * 启动时打开
     */
    private Boolean openOnStartup = true;


}
