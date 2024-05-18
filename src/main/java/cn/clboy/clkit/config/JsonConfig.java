package cn.clboy.clkit.config;

import cn.clboy.clkit.common.component.jackson.ClkitModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * json配置
 *
 * @author clboy
 * @date 2024/05/10 11:25:24
 */
@Configuration
public class JsonConfig {

    @Bean
    public ClkitModule clkitModule() {
        return new ClkitModule();
    }

}
