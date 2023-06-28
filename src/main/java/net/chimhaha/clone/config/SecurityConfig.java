package net.chimhaha.clone.config;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.config.auth.handler.CustomAccessDeniedHandler;
import net.chimhaha.clone.config.auth.handler.CustomAuthenticationEntryPoint;
import net.chimhaha.clone.config.auth.handler.CustomOAuth2LoginSuccessHandler;
import net.chimhaha.clone.config.auth.CustomOAuth2UserService;
import net.chimhaha.clone.config.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService oauth2UserService;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()

                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()

                .authorizeRequests()
                .anyRequest().permitAll()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oauth2UserService)

                .and()
                .successHandler(customOAuth2LoginSuccessHandler)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);

        return http.build();
    }
}
