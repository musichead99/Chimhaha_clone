package net.chimhaha.clone.config.auth;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Builder
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final Map<String, Object> attributes;
    private final Member member;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> member.getRole().getTitle());
        return authorities;
    }

    @Override
    public String getName() {
        return member.getName();
    }

    public Long getId() {
        return member.getId();
    }
}
