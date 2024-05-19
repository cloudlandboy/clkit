package cn.clboy.clkit.config;

import cn.clboy.clkit.ClkitApplication;
import cn.clboy.clkit.common.util.AppUtils;
import cn.clboy.clkit.common.web.GlobalExceptionController;
import cn.clboy.clkit.extension.filter.ExtensionResourceFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        UrlBasedCorsConfigurationSource cfgSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.addAllowedOriginPattern("*");
        cfg.addAllowedMethod("*");
        cfg.addAllowedHeader("*");
        cfg.setAllowCredentials(Boolean.TRUE);
        cfg.setMaxAge(Duration.ofDays(1));
        cfgSource.registerCorsConfiguration("/**", cfg);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new CorsFilter(cfgSource));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
        bean.setName("corsFilter");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<ExtensionResourceFilter> extensionResourceFilterRegistrationBean() {
        FilterRegistrationBean<ExtensionResourceFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ExtensionResourceFilter());
        bean.setName("extensionResourceFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 3);
        return bean;
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
        AppUtils.setAppProperties(appProperties);
    }

}
