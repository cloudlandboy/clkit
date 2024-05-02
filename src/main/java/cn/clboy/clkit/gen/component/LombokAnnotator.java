package cn.clboy.clkit.gen.component;

import cn.clboy.clkit.gen.dto.GenJavaClassDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JDefinedClass;
import lombok.*;
import org.jsonschema2pojo.AbstractAnnotator;

/**
 * Lombok注释者
 *
 * @author clboy
 * @date 2024/04/30 16:42:49
 */
@AllArgsConstructor
public class LombokAnnotator extends AbstractAnnotator {

    private final GenJavaClassDTO dto;

    @Override
    public void typeInfo(JDefinedClass clazz, JsonNode schema) {
        if (!Boolean.TRUE.equals(dto.getUseLombok())) {
            return;
        }

        if (this.useDataAnnotation()) {
            clazz.annotate(Data.class);
            return;
        }

        if (dto.getIncludeList().contains(GenJavaClassDTO.GETTER)) {
            clazz.annotate(Getter.class);
        }

        if (dto.getIncludeList().contains(GenJavaClassDTO.SETTER)) {
            clazz.annotate(Setter.class);
        }
        if (dto.getIncludeList().contains(GenJavaClassDTO.TOSTRING)) {
            clazz.annotate(ToString.class);
        }

        if (dto.getIncludeList().contains(GenJavaClassDTO.HASHCODE_AND_EQUALS)) {
            clazz.annotate(EqualsAndHashCode.class);
        }
    }

    /**
     * 使用@Data注解
     */
    private boolean useDataAnnotation() {
        return dto.getIncludeList().containsAll(GenJavaClassDTO.INCLUDE_ALL);
    }
}
