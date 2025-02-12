package cn.clboy.clkit.translate.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 翻译dto
 *
 * @author clboy
 * @date 2024/09/25 15:15:06
 */
@Data
public class TranslateDTO {
    @NotBlank
    private String from;
    @NotBlank
    private String to;
    @NotBlank
    private String text;
}
