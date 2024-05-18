package cn.clboy.clkit.common.component.jackson;

import cn.clboy.clkit.common.constants.enums.IValueLabelEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 值标签enum json序列化
 *
 * @author clboy
 * @date 2024/05/10 11:08:33
 */
public class ValueLabelEnumJsonSerializer extends JsonSerializer<IValueLabelEnum> {
    @Override
    public void serialize(IValueLabelEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getValue());
    }
}
