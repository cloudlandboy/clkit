package cn.clboy.clkit.gen.component;

import cn.clboy.clkit.gen.dto.GenJavaClassDTO;
import org.jsonschema2pojo.AnnotationStyle;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.SourceType;

/**
 * 基于JavaClassDTO的GenerationConfig
 *
 * @author clboy
 * @date 2024/04/30 14:30:51
 */
public class JsonGenJavaClassConfig extends DefaultGenerationConfig {
    private final GenJavaClassDTO dto;
    private final boolean nonUseLombok;

    public JsonGenJavaClassConfig(GenJavaClassDTO dto) {
        this.dto = dto;
        nonUseLombok = Boolean.FALSE.equals(dto.getUseLombok());
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.JSON;
    }

    @Override
    public AnnotationStyle getAnnotationStyle() {
        return AnnotationStyle.NONE;
    }

    @Override
    public boolean isSerializable() {
        return Boolean.TRUE.equals(dto.getSerializable());
    }

    @Override
    public boolean isIncludeAdditionalProperties() {
        return false;
    }

    @Override
    public boolean isIncludeHashcodeAndEquals() {
        return nonUseLombok && dto.getIncludeList().contains(GenJavaClassDTO.HASHCODE_AND_EQUALS);
    }

    @Override
    public boolean isIncludeToString() {
        return nonUseLombok && dto.getIncludeList().contains(GenJavaClassDTO.TOSTRING);
    }

    @Override
    public boolean isIncludeGetters() {
        return nonUseLombok && dto.getIncludeList().contains(GenJavaClassDTO.GETTER);
    }

    @Override
    public boolean isIncludeSetters() {
        return nonUseLombok && dto.getIncludeList().contains(GenJavaClassDTO.SETTER);
    }

    @Override
    public boolean isIncludeGeneratedAnnotation() {
        return false;
    }

}