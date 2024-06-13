package cn.clboy.clkit.common.component.jpa;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;

/**
 * json属性转换器
 *
 * @author clboy
 * @date 2024/04/18 16:12:25
 */
public abstract class JsonAttributeConverter<T> implements AttributeConverter<T, String> {
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final JavaType modelType;

    public JsonAttributeConverter() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.modelType = OBJECT_MAPPER.getTypeFactory().constructType(genericSuperclass.getActualTypeArguments()[0]);
    }

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(T attribute) {
        return OBJECT_MAPPER.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public T convertToEntityAttribute(String dbData) {
        return OBJECT_MAPPER.readValue(dbData, this.modelType);
    }

    /**
     * 获取模型类型
     */
    public JavaType getModelType() {
        return this.modelType;
    }
}
