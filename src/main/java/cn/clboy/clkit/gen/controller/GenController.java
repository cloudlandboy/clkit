package cn.clboy.clkit.gen.controller;

import cn.clboy.clkit.gen.dto.GenCrudDTO;
import cn.clboy.clkit.gen.dto.GenJavaClassDTO;
import cn.clboy.clkit.gen.service.GenService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Gen控制器
 *
 * @author clboy
 * @date 2024/04/23 15:23:41
 */
@RestController
@RequestMapping("gen")
@RequiredArgsConstructor
public class GenController {

    private final GenService genService;

    /**
     * 生成crud代码
     */
    @PostMapping("crud")
    public void genCrud(@Validated @RequestBody GenCrudDTO dto, HttpServletResponse response) {
        genService.genCrud(dto, response);
    }

    /**
     * 生成java类
     */
    @PostMapping("java_class")
    public void genJavaClass(@Validated @RequestBody GenJavaClassDTO dto, HttpServletResponse response) {
        genService.genJavaClass(dto, response);
    }
}
