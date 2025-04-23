package com.fiveguys.fivelogbackend.global.security.security.config;

import com.fiveguys.fivelogbackend.global.security.oauth.CustomOauth2AuthenticationSuccessHandler;
import com.fiveguys.fivelogbackend.global.security.security.CustomAuthenticationFilter;
import com.fiveguys.fivelogbackend.global.security.security.CustomAuthorizationRequestResolver;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
            "/api/**","/h2-console/**", "/actuator/**",
            "/user/join","/error", "/css/**", "/js/**",
            "/user/login", "/chat/completion/content"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
                   .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                   .authorizeHttpRequests( auth -> auth
                           .requestMatchers(permitURL).permitAll()
                           .anyRequest().authenticated())
                   .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                   );

           ; //h2-console 접근 허용
           return http.build();
    }
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트 도메인
        config.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(); // 빈 등록만 하고 사용자 추가 X
    }

}
