package cn.clboy.clkit.config;

import cn.clboy.clkit.common.component.security.ClkitUserDetailsService;
import cn.clboy.clkit.upms.service.ClkitUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置
 *
 * @author clboy
 * @date 2024/05/17 16:43:23
 */
@Configuration
public class SecurityConfig implements WebSecurityCustomizer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((req) -> {
            req.anyRequest().authenticated();
        });
        http.formLogin();
        http.sessionManagement().disable();
        http.sessionManagement().disable();
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService(ClkitUserService clkitUserService) {
        return new ClkitUserDetailsService(clkitUserService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void customize(WebSecurity web) {
        web.debug(true);
    }
}
