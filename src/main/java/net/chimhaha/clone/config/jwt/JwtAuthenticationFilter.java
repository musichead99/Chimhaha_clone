package net.chimhaha.clone.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.config.auth.CustomOAuth2UserService;
import net.chimhaha.clone.exception.CustomException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String unparsedToken = request.getHeader("Authorization");
        log.info("unparsedToken: {}", unparsedToken);

        String token = parseToken(unparsedToken);

        boolean validateResult = false;
        try {
            validateResult = jwtTokenProvider.validate(token);
        } catch(CustomException e) {
            request.setAttribute("exception", e);
        }

        if(validateResult) {
            setAuthenticationAtSecurityContext(token, request);
        }

        filterChain.doFilter(request, response);
    }

    private String parseToken(String unparsedToken) {
        if(StringUtils.hasText(unparsedToken) && unparsedToken.startsWith("Bearer ")) {
            return unparsedToken.substring(7);
        }
        return null;
    }

    private void setAuthenticationAtSecurityContext(String token, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getIdFromJwt(token);
        OAuth2User oAuth2User = customOAuth2UserService.findOAuth2UserById(userId);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                oAuth2User,
                null,
                oAuth2User.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
