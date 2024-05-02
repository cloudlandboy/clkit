package cn.clboy.clkit.common.component.jpa;

import com.fasterxml.jackson.databind.JavaType;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组json属性转换器
 *
 * @author clboy
 * @date 2024/04/26 14:31:13
 */
public class ListJsonAttributeConverter<T> extends JsonAttributeConverter<List<T>> {
    private final JavaType ListModelType;

    public ListJsonAttributeConverter() {
        super();
        this.ListModelType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, this.getModelType());
    }

    @Override
    @SneakyThrows
    public List<T> convertToEntityAttribute(String dbData) {
        return OBJECT_MAPPER.readValue(dbData, ListModelType);
    }
}
