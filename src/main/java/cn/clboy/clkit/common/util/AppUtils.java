package cn.clboy.clkit.common.util;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * 资源工具
 *
 * @author clboy
 * @date 2024/04/18 11:29:11
 */
public class AppUtils {

    private static String dataDirPath;
    private static final String HOME_DIR_PATH = new ApplicationHome().getDir().getAbsolutePath();


    /**
     * 设置数据目录路径
     *
     * @param dataDirPath 数据目录路径
     */
    public static void setDataDirPath(String dataDirPath) {
        AppUtils.dataDirPath = dataDirPath;
    }


    /**
     * 获取数据目录路径
     */
    public static String getDataDirPath() {
        return AppUtils.dataDirPath;
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

}
