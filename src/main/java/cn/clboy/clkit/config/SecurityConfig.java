package cn.clboy.clkit.config;

import cn.clboy.clkit.common.component.security.*;
import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.upms.service.ClkitTokenService;
import cn.clboy.clkit.upms.service.ClkitUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置
 *
 * @author clboy
 * @date 2024/05/17 16:43:23
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig implements WebSecurityCustomizer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(ClkitUserService clkitUserService) {
        return new ClkitUserDetailsService(clkitUserService);
    }

    @Bean
    public AuthenticationManager authenticationManager(ClkitTokenService tokenService,
                                                       UserDetailsService userDetailsService) {
        List<AuthenticationProvider> providers = new ArrayList<>(2);
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setPasswordEncoder(this.passwordEncoder());
        daoProvider.setUserDetailsService(userDetailsService);
        providers.add(daoProvider);
        providers.add(daoProvider);
        providers.add(new RefreshTokenAuthenticationProvider(tokenService,
                (ClkitUserDetailsService) userDetailsService));
        return new ProviderManager(providers);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClkitTokenService tokenService,
                                                   UserDetailsService userDetailsService, ObjectMapper objectMapper,
                                                   AuthenticationManager authenticationManager) throws Exception {

        http.authorizeHttpRequests((req) -> {
            req.anyRequest().permitAll();
        });
        http.csrf().disable();
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher(ClkitConstant.LOGOUT_API_URL, HttpMethod.POST.name()))
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        BearerTokenFilter bearerTokenFilter = new BearerTokenFilter(tokenService, (ClkitUserDetailsService) userDetailsService);
        http.addFilterBefore(bearerTokenFilter, LogoutFilter.class);

        ClkitAuthenticationConverter authenticationConverter = new ClkitAuthenticationConverter();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, authenticationConverter);
        authenticationFilter.setRequestMatcher(new AntPathRequestMatcher(ClkitConstant.AUTH_API_URL, HttpMethod.POST.name()));
        authenticationFilter.setSuccessHandler(new ClkitAuthenticationSuccessHandler(objectMapper, tokenService));
        authenticationFilter.setFailureHandler(new ClkitAuthenticationFailureHandler(objectMapper));
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Override
    public void customize(WebSecurity web) {
        web.debug(false);
        //扩展
        web.ignoring().antMatchers("/extension/**", HttpMethod.GET.name());
    }
}
