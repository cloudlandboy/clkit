package cn.clboy.clkit.gen.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 生成Java类dto
 *
 * @author clboy
 * @date 2024/04/30 13:48:56
 */
@Data
public class GenJavaClassDTO {

    public static final String SETTER = "setter";
    public static final String GETTER = "getter";
    public static final String TOSTRING = "toString";
    public static final String HASHCODE_AND_EQUALS = "hashcodeAndEquals";
    public static final Set<String> INCLUDE_ALL = new HashSet<>(Arrays.asList(GenJavaClassDTO.GETTER,
            GenJavaClassDTO.SETTER, GenJavaClassDTO.TOSTRING, GenJavaClassDTO.HASHCODE_AND_EQUALS));

    /**
     * 包路径
     */
    private String packagePath;

    /**
     * 类名
     */
    private String className;

    /**
     * 源代码
     */
    private String sourceCode;

    /**
     * 源类型
     */
    private String sourceType;

    /**
     * 使用Lombok
     */
    private Boolean useLombok = true;

    /**
     * 包括列表
     */
    private Set<String> includeList = INCLUDE_ALL;

    /**
     * 实现Serializable接口
     */
    private Boolean serializable = true;

    /**
     * 用于预览
     */
    private Boolean forPreview = false;

}
