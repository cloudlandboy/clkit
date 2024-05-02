package cn.clboy.clkit.app.controller;

import cn.clboy.clkit.app.dto.ExportDataDTO;
import cn.clboy.clkit.app.service.AppService;
import cn.clboy.clkit.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * app Controller
 *
 * @author clboy
 * @date 2024/04/28 16:50:07
 */
@RestController
@RequestMapping("app")
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;

    /**
     * 导出数据
     *
     * @param dtoList  dto列表
     * @param response 响应
     */
    @PostMapping("/export")
    public void exportData(@RequestBody List<ExportDataDTO> dtoList, HttpServletResponse response) {
        appService.exportData(dtoList, response);
    }


    /**
     * 导入数据
     *
     * @param file 文件
     */
    @PostMapping("/import")
    public void importData(MultipartFile file) {
        appService.importData(file);
    }

    /**
     * 获取版本
     */
    @GetMapping("version")
    public ApiResult<String> getVersion() {
        return ApiResult.ok(appService.getVersion());
    }
}
