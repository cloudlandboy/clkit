package cn.clboy.clkit.config;

import cn.clboy.clkit.ClkitApplication;
import cn.clboy.clkit.common.web.GlobalExceptionController;
import cn.clboy.clkit.util.AppUtils;
import cn.hutool.core.io.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.time.Duration;

/**
 * 应用程序配置
 *
 * @author clboy
 * @date 2024/04/18 14:40:07
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig implements InitializingBean, WebMvcConfigurer {

    private final AppProperties appProperties;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource cfgSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.addAllowedOriginPattern("http://127.0.0.1:[*]");
        cfg.addAllowedMethod("*");
        cfg.addAllowedHeader("*");
        cfg.setAllowCredentials(Boolean.TRUE);
        cfg.setMaxAge(Duration.ofDays(1));
        cfgSource.registerCorsConfiguration("/**", cfg);
        return new CorsFilter(cfgSource);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //clkit-ui dist
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        String appPackage = ClkitApplication.class.getPackage().getName();
        configurer.addPathPrefix("/api", clazz -> {
            if (GlobalExceptionController.class.equals(clazz)) {
                return false;
            }
            if (!(clazz.getPackage().getName().startsWith(appPackage))) {
                return false;
            }
            return clazz.isAnnotationPresent(RestController.class);
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.hasText(appProperties.getDataDirPath())) {
            appProperties.setDataDirPath(AppProperties.DEFAULT_DATA_DIR_PATH);
        }
        File appDirfile = new File(FileUtil.getAbsolutePath(appProperties.getDataDirPath()));
        Assert.isTrue(appDirfile.exists() || appDirfile.mkdir(), "创建数据目录出错");
        AppUtils.setDataDirPath(appDirfile.getAbsolutePath());
    }
}
