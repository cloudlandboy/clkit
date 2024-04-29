package cn.clboy.clkit.app.dto;

import lombok.Data;

import java.util.List;

/**
 * 导出数据dto
 *
 * @author clboy
 * @date 2024/04/28 16:55:12
 */
@Data
public class ExportDataDTO {

    /**
     * 模块
     */
    private String module;

    /**
     * ids，仅导出选中的数据
     */
    private List<Long> ids;
}
