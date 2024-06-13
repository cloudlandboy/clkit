package cn.clboy.clkit.common.holder;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 对象映射器持有者
 *
 * @author clboy
 * @date 2024/06/05 16:43:46
 */
public class ObjectMapperHolder {
    private static ObjectMapper OBJECT_MAPPER;

    public static ObjectMapper getObjectMapper() {
        if (ObjectMapperHolder.OBJECT_MAPPER != null) {
            return ObjectMapperHolder.OBJECT_MAPPER;
        }
        synchronized (ObjectMapperHolder.class) {
            if (ObjectMapperHolder.OBJECT_MAPPER != null) {
                return ObjectMapperHolder.OBJECT_MAPPER;
            }
            ObjectMapperHolder.OBJECT_MAPPER = SpringUtil.getBean(ObjectMapper.class);
        }
        return ObjectMapperHolder.OBJECT_MAPPER;
    }
}
