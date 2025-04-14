package com.fiveguys.fivelogbackend.global.security.security;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauthId = oAuth2User.getName();
        String providerTypeCode = userRequest
                .getClientRegistration() // ClientRegistration
                .getRegistrationId()     // String
                .toUpperCase(Locale.getDefault());

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, String> attributesProperties = (Map<String, String>) attributes.get("properties");
        String nickname = attributesProperties.get("nickname");
//        String profileImgUrl = attributesProperties.get("profile_image");
        String email = providerTypeCode + "__" + oauthId;

        User user = userService.modifyOrJoin(email, nickname);

        return new SecurityUser(
                 user.getId(),
                user.getEmail(),
                 "",
                user.getNickname(),
                user.getAuthorities()
        );
    }


}
