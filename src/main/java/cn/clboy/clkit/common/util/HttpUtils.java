package cn.clboy.clkit.common.util;

import cn.hutool.core.net.URLEncodeUtil;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;

/**
 * http utils
 *
 * @author clboy
 * @date 2024/04/30 16:23:59
 */
public class HttpUtils {

    /**
     * 写文件
     *
     * @param response    响应
     * @param data        数据
     * @param contentType 内容类型
     * @param filename    文件名
     */
    @SneakyThrows
    public static void writeFile(HttpServletResponse response, byte[] data, String contentType, String filename) {
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncodeUtil.encode(filename));
        response.getOutputStream().write(data);
    }

}
