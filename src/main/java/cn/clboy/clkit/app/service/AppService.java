package cn.clboy.clkit.app.service;

import cn.clboy.clkit.app.dto.ExportDataDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * app服务
 *
 * @author clboy
 * @date 2024/04/28 16:51:15
 */
public interface AppService {

    /**
     * 导出数据
     *
     * @param dtoList  dtoList
     * @param response 响应
     */
    void exportData(List<ExportDataDTO> dtoList, HttpServletResponse response);

    /**
     * 导入数据
     *
     * @param file 文件
     */
    void importData(MultipartFile file);

    /**
     * 获取版本
     */
    String getVersion();

}
