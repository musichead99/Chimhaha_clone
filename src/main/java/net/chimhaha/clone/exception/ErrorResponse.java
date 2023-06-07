package net.chimhaha.clone.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {
    private LocalDateTime localDateTime = LocalDateTime.now();
    private int code;
    private String error;
    private String message;
    private List<ErrorDetail> details = new ArrayList<>();

    @Getter
    private static class ErrorDetail {
        private String field;
        private String detailMessage;

        public ErrorDetail(String field, String detailMessage) {
            this.field = field;
            this.detailMessage = detailMessage;
        }

        public ErrorDetail(FieldError fieldError) {
            this.field = fieldError.getField();
            this.detailMessage = fieldError.getDefaultMessage();
        }
    }

    private void parseAndSetErrorCode(ErrorCode errorCode) {
        this.code = errorCode.getHttpStatus().value();
        this.error = errorCode.name();
        this.message = errorCode.getMessage();
    }

    private ErrorResponse(ErrorCode errorCode) {
        parseAndSetErrorCode(errorCode);
    }

    private ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
        parseAndSetErrorCode(errorCode);
        details = errors.stream().map(ErrorDetail::new).collect(Collectors.toList());
    }

    private ErrorResponse(ErrorCode errorCode, String message) {
        parseAndSetErrorCode(errorCode);
        this.details.add(new ErrorDetail("", message));
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ErrorResponse(errorCode, bindingResult.getFieldErrors());
    }
}
