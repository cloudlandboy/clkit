package cn.clboy.clkit.config;

import cn.clboy.clkit.common.util.AppUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * 应用程序属性
 *
 * @author clboy
 * @date 2024/04/18 14:27:45
 */
@Data
@Validated
@ConfigurationProperties(prefix = "clkit")
public class AppProperties {

    public static final String DEFAULT_DATA_DIR_PATH = "~/.clkit";
    public static final String DEFAULT_EXTENSION_DIR_PATH = AppUtils.getHomeFile("clkit_extensions").getAbsolutePath();

    /**
     * 版本
     */
    @NotBlank(message = "invalid version")
    public String version;

    /**
     * 数据目录路径
     */
    @NotBlank(message = "invalid dataDirPath")
    private String dataDirPath = DEFAULT_DATA_DIR_PATH;

    /**
     * 扩展目录路径
     */
    @NotBlank(message = "invalid extensionDirPath")
    private String extensionDirPath = DEFAULT_EXTENSION_DIR_PATH;

    /**
     * 启动时打开
     */
    private Boolean openOnStartup = true;


}
