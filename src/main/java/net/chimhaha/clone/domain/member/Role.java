package net.chimhaha.clone.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "일반 유저"),
    BOARD_MANAGER("ROLE_BOARD_MANAGER", "게시판 관리자"),
    ADMIN("ROLE_ADMIN", "전체 관리자");

    private final String title;
    private final String description;
}
