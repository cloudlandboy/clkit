package cn.clboy.clkit.common.constants.enums;

import cn.clboy.clkit.common.vo.ValueLabelVO;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ValueLabel类型枚举规范接口
 *
 * @author clboy
 * @date 2024/05/06 10:26:55
 */
public interface IValueLabelEnum<E extends IValueLabelEnum> {

    /**
     * 缓存
     */
    Map<String, Map<String, ? extends IValueLabelEnum>> CACHE = new ConcurrentHashMap<>();

    /**
     * 获取标签
     */
    String getLabel();

    /**
     * 获取值
     */
    String getValue();


    /**
     * 获取通过值
     *
     * @param vle    VLE
     * @param orElse 否则
     */
    static <E extends IValueLabelEnum> E getByValue(Class<E> vle, String value, E orElse) {
        return Optional.ofNullable(getByValue(vle, value)).orElse(orElse);
    }

    /**
     * 获取通过值
     *
     * @param vle   VLE
     * @param value 值
     */
    static <E extends IValueLabelEnum> E getByValue(Class<E> vle, String value) {
        return toValueMap(vle).get(value);
    }

    /**
     * 获取通过值或者返回调用者
     *
     * @param value 值
     */
    default E orCallerByValue(String value) {
        if (this.valueEq(value)) {
            return (E) this;
        }
        Class<E> clazz = (Class<E>) this.getClass();
        return getByValue(clazz, value, (E) this);
    }

    /**
     * value与给定值相等
     *
     * @param value 值
     */
    default boolean valueEq(String value) {
        return this.getValue().equals(value);
    }

    /**
     * 值映射
     *
     * @param clazz clazz
     */
    static <E extends IValueLabelEnum> Map<String, E> toValueMap(Class<E> clazz) {
        return (Map<String, E>) CACHE.computeIfAbsent(clazz.getName(), className -> {
            IValueLabelEnum[] enumConstants = clazz.getEnumConstants();
            Map<String, E> map = CollectionUtils.newHashMap(enumConstants.length);
            for (IValueLabelEnum em : enumConstants) {
                map.put(em.getValue(), (E) em);
            }
            return map;
        });
    }

    /**
     * 转换为value:label的map
     *
     * @param clazz clazz
     */
    static Map<String, String> toValueLabelMap(Class<? extends IValueLabelEnum> clazz) {
        IValueLabelEnum[] enumConstants = clazz.getEnumConstants();
        Map<String, String> map = CollectionUtils.newHashMap(enumConstants.length);
        for (IValueLabelEnum em : enumConstants) {
            map.put(em.getValue(), em.getLabel());
        }
        return map;
    }

    /**
     * 到值标签数据
     *
     * @param clazz clazz
     */
    static List<ValueLabelVO> toValueLabelVO(Class<? extends IValueLabelEnum> clazz) {
        return Arrays.stream(clazz.getEnumConstants()).map(em -> {
            ValueLabelVO valueLabelVO = new ValueLabelVO();
            valueLabelVO.setValue(em.getValue());
            valueLabelVO.setLabel(em.getLabel());
            return valueLabelVO;
        }).collect(Collectors.toList());
    }

}
