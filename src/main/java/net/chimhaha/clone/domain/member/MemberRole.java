package net.chimhaha.clone.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    BAESEONGJAE("ROLE_BAEGUSON", "배성재"),
    JUHOMIN("ROLE_PEARL", "주호민"),
    SWAB("ROLE_SWAB", "승우아빠"),
    KWAKJUNBIN("ROLE_KWAK", "곽튜브"),
    JOOWOOJAE("ROLE_WOOJAE", "주우재"),
    PARKJEONGMIN("ROLE_PARKJEONGMIN", "박정민"),
    ORBIT("ROLE_ORBIT", "궤도"),
    KWAKMINSOO("ROLE_KWAKMINSOO", "곽민수"),
    MAGICPARK("ROLE_MAGICPARK", "매직박"),
    PANIBOTTLE("ROLE_PANIBOTTLE", "빠니보틀"),
    CHICKEN_ANGLE("ROLE_CHICKEN_ANGLE", "통닭천사"),
    CHULMYEONSUSIM("ROLE_CHULMYEONSUSIM", "철면수심"),
    DANGUN("ROLE_DANGUN", "단군"),
    KIMPOONG("ROLE_KIMPOONG", "김풍"),
    IMSEMO("ROLE_IMSEMO", "임세모"),
    SIMYUNSOO("ROLE_SIMYUNSOO", "심윤수"),

    USER(ROLES.USER, "일반 유저"),
    ADMIN(ROLES.ADMIN, "전체 관리자");

    public static class ROLES {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }

    private final String title;
    private final String description;
}
