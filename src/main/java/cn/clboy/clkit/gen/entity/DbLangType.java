package cn.clboy.clkit.gen.entity;

import cn.clboy.clkit.common.entity.BaseEntity;
import cn.clboy.clkit.common.constants.enums.DbPlatformEnum;
import cn.clboy.clkit.common.entity.IUniqueNameEntity;
import cn.clboy.clkit.gen.dto.LangTypeMatchDTO;
import cn.clboy.clkit.gen.jpa.LangTypeMatchListAttributeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * db类型
 *
 * @author clboy
 * @date 2024/04/18 15:49:45
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class DbLangType extends BaseEntity implements IUniqueNameEntity {

    /**
     * 唯一名称
     */
    @Comment("唯一名称")
    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * 数据库平台
     */
    @Comment("数据库平台")
    @NotNull
    private DbPlatformEnum dbPlatform;

    /**
     * 语言类型
     */
    @Comment("语言类型")
    @NotBlank
    private String langType;

    /**
     * 锁定
     */
    @Comment("是否锁定")
    @NotNull
    private Boolean locked;

    /**
     * 语言类型与数据库类型匹配规则列表
     */
    @Lob
    @Comment("语言类型与数据库类型匹配规则列表")
    @NotEmpty
    @Convert(converter = LangTypeMatchListAttributeConverter.class)
    private List<LangTypeMatchDTO> matchList;

    public void setLangType(String langType) {
        this.langType = langType.toUpperCase();
    }
}
