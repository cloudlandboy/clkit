package cn.clboy.clkit.common.component.jackson;

import cn.clboy.clkit.common.constants.enums.IValueLabelEnum;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 值标签enum json解串器
 *
 * @author clboy
 * @date 2024/05/10 11:34:20
 */
public class ValueLabelEnumJsonDeserializer<E extends IValueLabelEnum<E>> extends JsonDeserializer<E> {
    private final Class<E> enumClass;

    public ValueLabelEnumJsonDeserializer(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JacksonException {
        return IValueLabelEnum.getByValue(this.enumClass, p.getValueAsString());
    }
}
