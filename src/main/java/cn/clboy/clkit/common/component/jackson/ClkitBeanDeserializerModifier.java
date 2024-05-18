package cn.clboy.clkit.common.component.jackson;

import cn.clboy.clkit.common.constants.enums.IValueLabelEnum;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

/**
 * clKit bean 反序列化修改器
 *
 * @author clboy
 * @date 2024/05/16 16:09:04
 */
public class ClkitBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        if (IValueLabelEnum.class.isAssignableFrom(type.getRawClass())) {
            Class<? extends IValueLabelEnum> rawClass = (Class<? extends IValueLabelEnum>) type.getRawClass();
            return new ValueLabelEnumJsonDeserializer<>(rawClass);
        }
        return super.modifyEnumDeserializer(config, type, beanDesc, deserializer);
    }
}
