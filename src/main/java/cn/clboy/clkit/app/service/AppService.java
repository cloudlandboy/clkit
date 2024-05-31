package cn.clboy.clkit.app.service;

import cn.clboy.clkit.app.dto.ExportDataDTO;
import cn.clboy.clkit.common.vo.ValueLabelVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取字典
     *
     * @param types 类型
     */
    Map<String, List<ValueLabelVO>> getDict(String types);

    /**
     * 获取字典js常量声明
     */
    String getDictJsConstDeclare();

    /**
     * 获取权限js常量声明
     */
    String getPermissionJsConstDeclare();

}
