package cn.clboy.clkit.app.controller;

import cn.clboy.clkit.app.dto.ExportDataDTO;
import cn.clboy.clkit.app.service.AppService;
import cn.clboy.clkit.common.vo.ValueLabelVO;
import cn.clboy.clkit.common.web.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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
    @PreAuthorize("@authChecker.hasPermission('app_export')")
    public void exportData(@RequestBody List<ExportDataDTO> dtoList, HttpServletResponse response) {
        appService.exportData(dtoList, response);
    }


    /**
     * 导入数据
     *
     * @param file 文件
     */
    @PostMapping("/import")
    @PreAuthorize("@authChecker.hasPermission('app_import')")
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

    /**
     * 获取字典
     */
    @GetMapping("dict")
    @Operation(tags = "获取字典")
    @Parameter(name = "types", description = "字典类型,多个英文逗号分隔")
    public ApiResult<Map<String, List<ValueLabelVO>>> getDict(String types) {
        return ApiResult.ok(appService.getDict(types));
    }

    /**
     * 获取字典js常量声明
     */
    @GetMapping("dict_js_const_declare")
    @Operation(tags = "获取字典js常量声明")
    public String getDictJsConstDeclare() {
        return appService.getDictJsConstDeclare();
    }

    /**
     * 获取权限js常量声明
     */
    @GetMapping("permission_js_const_declare")
    @Operation(tags = "获取字典js常量声明")
    public String getPermissionJsConstDeclare() {
        return appService.getPermissionJsConstDeclare();
    }
}
