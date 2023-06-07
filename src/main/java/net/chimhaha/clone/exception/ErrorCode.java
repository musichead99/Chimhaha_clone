package net.chimhaha.clone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    IMAGES_NOT_ATTACHED(HttpStatus.BAD_REQUEST, "다른 게시글에 첨부된 이미지는 첨부할 수 없습니다."),
    FILE_NOT_UPLOADED(HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),
    FILE_LOAD_FAILED(HttpStatus.BAD_REQUEST, "파일을 읽어들이는 데 실패했습니다."),
    FILE_NOT_EXIST(HttpStatus.BAD_REQUEST, "해당 파일이 존재하지 않습니다."),


    // 404 Not Found
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),
    BOARDS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시판을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
    POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    COMMENTS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    IMAGES_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지를 DB에서 찾을 수 없습니다."),


    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "해당 메소드는 이 url에서 사용할 수 없습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String message;
}
