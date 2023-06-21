package net.chimhaha.clone.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.member.MemberRepository;
import net.chimhaha.clone.domain.member.Role;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        ClientRegistration clientRegistration = userRequest.getClientRegistration();


        log.info("Attribute: {}", attributes.toString());
        log.info("ClientRegistration: {}", clientRegistration);

        OAuth2UserInfo userInfo = OAuth2UserInfo.of(attributes, clientRegistration);

        Member member = saveOrUpdate(userInfo);

        return new CustomOAuth2User(attributes, member);
    }

    @Transactional
    public Member saveOrUpdate(OAuth2UserInfo userInfo) {
        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .map(entity -> entity.update(userInfo.getProfileImage()))
                .orElse(Member.builder()
                        .provider(userInfo.getProvider())
                        .name(userInfo.getName())
                        .nickname(userInfo.getNickname())
                        .email(userInfo.getEmail())
                        .profileImage(userInfo.getProfileImage())
                        .role(Role.USER)
                        .build());

        return memberRepository.save(member);
    }
}
