package cn.clboy.clkit.translate.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.translate.dto.TranslateDTO;
import cn.clboy.clkit.translate.service.TranslateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 翻译控制器
 *
 * @author clboy
 * @date 2024/09/25 14:52:18
 */
@RestController
@Tag(name = "翻译管理")
@RequestMapping("/translate")
public class TranslateController {

    private final Map<String, TranslateService> translateServiceMap;

    public TranslateController(List<TranslateService> serviceList) {
        this.translateServiceMap = serviceList.stream()
                .collect(Collectors.toMap(TranslateService::getKey, Function.identity()));
    }

    /**
     * 翻译
     *
     * @param key 关键
     * @param dto DTO
     */
    @PostMapping("/{key}")
    public ApiResult<String> translate(@PathVariable String key, @Validated @RequestBody TranslateDTO dto) {
        TranslateService translateService = translateServiceMap.get(key);
        Assert.notNull(translateService, "不支持的key：" + key);
        return ApiResult.ok(translateService.translate(dto));
    }
}
