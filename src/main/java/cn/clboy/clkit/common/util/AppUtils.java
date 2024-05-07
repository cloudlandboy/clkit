package cn.clboy.clkit.common.util;

import cn.clboy.clkit.config.AppProperties;
import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源工具
 *
 * @author clboy
 * @date 2024/04/18 11:29:11
 */
public class AppUtils {

    @Getter
    private static String dataDirPath;

    @Getter
    private static String extensionDirPath;

    @Getter
    private static AppProperties appProperties;
    private static final String HOME_DIR_PATH = new ApplicationHome().getDir().getAbsolutePath();


    /**
     * 设置应用程序属性
     *
     * @param appProperties 应用程序属性
     */
    public static void setAppProperties(AppProperties appProperties) {
        File appDirfile = new File(FileUtil.getAbsolutePath(appProperties.getDataDirPath()));
        Assert.isTrue(appDirfile.exists() || appDirfile.mkdir(), "创建数据目录出错");
        AppUtils.dataDirPath = appDirfile.getAbsolutePath();
        File extensionDirFile = new File(FileUtil.getAbsolutePath(appProperties.getExtensionDirPath()));
        Assert.isTrue(extensionDirFile.exists() || extensionDirFile.mkdir(), "创建扩展目录出错");
        AppUtils.extensionDirPath = extensionDirFile.getAbsolutePath();
    }

    /**
     * 获取应用程序主目录路径
     */
    public static String getHomeDirPath() {
        return AppUtils.HOME_DIR_PATH;
    }

    /**
     * 获取数据目录下的文件
     *
     * @param path 路径
     */
    public static File getDataFile(String path) {
        return new File(dataDirPath, path);
    }

    /**
     * 获取主目录下文件
     *
     * @param path 路径
     */
    public static File getHomeFile(String path) {
        return new File(HOME_DIR_PATH, path);
    }

    /**
     * 获取扩展目录文件
     *
     * @param id ID
     */
    public static File getExtensionDirFile(Long id) {
        return new File(extensionDirPath, id.toString());
    }

    /**
     * 获取扩展文件
     *
     * @param id   ID
     * @param path 路径
     */
    public static File getExtensionFile(Long id, String path) {
        return new File(getExtensionDirFile(id), path);
    }

    /**
     * 列出存在的扩展id
     */
    public static List<Long> listExistsExtensionIds() {
        File file = new File(extensionDirPath);
        File[] dirs = file.listFiles(File::isDirectory);
        if (dirs == null) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>(dirs.length);
        for (File dir : dirs) {
            try {
                ids.add(Long.parseLong(dir.getName()));
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        return ids;
    }
}
