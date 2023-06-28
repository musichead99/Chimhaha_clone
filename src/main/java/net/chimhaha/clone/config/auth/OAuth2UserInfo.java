package net.chimhaha.clone.config.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class OAuth2UserInfo {

    private final String provider;
    private final String name;
    private final String nickname;
    private final String email;
    private final String profileImage;
    private final Map<String, Object> attributes;

    public static OAuth2UserInfo of(Map<String, Object> attributes, ClientRegistration clientRegistration) {
        String registrationId = clientRegistration.getRegistrationId();

        if(registrationId.equals("naver")) {
            return ofNaver(attributes, clientRegistration);
        }

        return null;
    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes, ClientRegistration clientRegistration) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return new OAuth2UserInfo(
                clientRegistration.getRegistrationId(),
                (String) response.get("name"),
                (String) response.get("nickname"),
                (String) response.get("email"),
                (String) response.get("profile_image"),
                response
        );

    }
}
