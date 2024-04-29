package cn.clboy.clkit.app.handler;

import cn.clboy.clkit.common.handler.HandlerHolder;

import java.util.List;

/**
 * 应用程序数据处理程序
 *
 * @author clboy
 * @date 2024/04/28 16:58:58
 */
public interface AppDataHandler<T> {
    HandlerHolder<String, AppDataHandler> HOLDER = new HandlerHolder<>();

    /**
     * 获取模块名称
     */
    String getModuleName();

    /**
     * 导出数据
     *
     * @param ids ids
     */
    List<T> exportData(List<Long> ids);

    /**
     * 导入数据
     *
     * @param dataList 数据列表
     */
    void importData(List<T> dataList);

    /**
     * 获取导出模型类
     */
    Class<T> getExportModelClass();

}
