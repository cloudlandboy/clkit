package cn.clboy.clkit.common.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典常量
 *
 * @author clboy
 * @date 2024/05/15 15:08:52
 */
public interface DictConstant {
    Logger LOG = LoggerFactory.getLogger(DictConstant.class);

    Map<String, DictDeclare> DICT_MAP = new HashMap<>();

    /**
     * 注册字典
     *
     * @param dictDeclare 字典声明
     */
    static void registerDict(DictDeclare dictDeclare) {
        LOG.info("注册字典: {}", dictDeclare);
        DICT_MAP.put(dictDeclare.getType(), dictDeclare);
    }
}
