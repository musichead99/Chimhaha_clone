package net.chimhaha.clone.config;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.config.auth.CustomOAuth2LoginSuccessHandler;
import net.chimhaha.clone.config.auth.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService oauth2UserService;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .headers().frameOptions().sameOrigin()

                .and()
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()

                .authorizeRequests()
                .antMatchers("/h2-console/**", "/oauth2/authorization/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oauth2UserService)

                .and()
                .successHandler(customOAuth2LoginSuccessHandler)

                .and()
                .build();
    }
}
