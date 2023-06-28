package net.chimhaha.clone.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    USER(ROLES.USER, "일반 유저"),
    MANAGER(ROLES.MANAGER, "게시판 관리자"),
    ADMIN(ROLES.ADMIN, "전체 관리자");

    public static class ROLES {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String USER = "ROLE_USER";
    }

    private final String title;
    private final String description;
}
