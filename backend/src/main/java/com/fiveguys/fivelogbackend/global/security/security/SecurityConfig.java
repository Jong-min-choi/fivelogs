package com.fiveguys.fivelogbackend.global.security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final CustomOauth2AuthenticationSuccessHandler customOauth2AuthenticationSuccessHandler;
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
    private final String[] permitURL = {"/login",
            "/v3/api-docs/**", "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/**","/h2-console/**", "/actuator/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
                   .authorizeHttpRequests( auth -> auth
                           .requestMatchers(permitURL).permitAll()
                           .anyRequest().authenticated())
                   .cors(cors -> cors.disable())
                   .csrf(csrf -> csrf.disable())
                   .formLogin( form -> form.disable())
                   .httpBasic(httpBasic -> httpBasic.disable())
                   .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                   .formLogin(
                           AbstractHttpConfigurer::disable
                   )
                   .sessionManagement((sessionManagement) -> sessionManagement
                           .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                   )
                   .oauth2Login( oauth2Login ->
                        oauth2Login.successHandler(customOauth2AuthenticationSuccessHandler)
                                .authorizationEndpoint( authorizationEndpoint ->
                                        authorizationEndpoint
                                                .authorizationRequestResolver(customAuthorizationRequestResolver)
                                )
                   )
                   .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

           ; //h2-console 접근 허용


           return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(); // 빈 등록만 하고 사용자 추가 X
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
