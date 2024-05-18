package cn.clboy.clkit.common.component.jpa;

import cn.clboy.clkit.common.constants.enums.IValueLabelEnum;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;

/**
 * 抽象值标签列举转换器
 *
 * @author clboy
 * @date 2024/05/10 10:54:54
 */
public abstract class AbstractValueLabelEnumConverter<E extends IValueLabelEnum<E>> implements AttributeConverter<E, String> {
    protected final Class<E> enumClass;

    public AbstractValueLabelEnumConverter() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.enumClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute.getValue();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        return IValueLabelEnum.getByValue(this.enumClass, dbData);
    }
}
