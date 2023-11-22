package com.spring.course.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Configuration class for handling security-related configurations.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    public static final String[] PERMITTED_ROUTES = {
            "/api/v1/orders/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/api/auth/register",
            "/api/auth/authenticate",
    };

    /**
     * Defines the security filter chain.
     *
     * @param httpSecurity The HttpSecurity object to configure security settings.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an exception occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity
                .authorizeHttpRequests(rQ -> {
                    rQ.requestMatchers(PERMITTED_ROUTES).permitAll();
                    rQ.anyRequest().authenticated();
                });
        httpSecurity.cors(Customizer.withDefaults());

        httpSecurity.sessionManagement(sessionAuthenticationStrategy ->
                sessionAuthenticationStrategy.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
