package net.chimhaha.clone.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        sendErrorResponse(request, response, authException);
    }

    private void sendErrorResponse(HttpServletRequest request,
                           HttpServletResponse response,
                           AuthenticationException authException) throws IOException {
        CustomException e = (CustomException) request.getAttribute("exception");

        if(e != null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8"); // 반환 메시지에서 한글이 제대로 보이지 않는 문제 때문에 인코딩 기법 명시
            response.setStatus(e.getErrorCode().getHttpStatus().value()); // http status code 설정
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(e.getErrorCode()))); // 반환 메시지
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getLocalizedMessage());
        }
    }
}
