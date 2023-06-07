package com.mini.ecommerceapp.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    static class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        @Override
        public AbstractAuthenticationToken convert(final Jwt jwt) {
            Collection<GrantedAuthority> authorities = extractResourceRoles(jwt);
            return new JwtAuthenticationToken(jwt, authorities);
        }

        private Collection<GrantedAuthority> extractResourceRoles(final Jwt jwt) {
            Map<String, Object> resourceAccess = jwt.getClaim("realm_access");
            if (Objects.isNull(resourceAccess) || Objects.isNull(resourceAccess.get("roles"))) {
                return Collections.emptySet();
            }
            return ((Collection<String>) resourceAccess.get("roles"))
                    .stream()
                    .filter(s -> StringUtils.startsWith(s, "ROLE_"))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/*/add**", "/*/edit**", "/*/delete**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(new CustomAuthenticationConverter())
                        )
                );
        return http.build();
    }
}
