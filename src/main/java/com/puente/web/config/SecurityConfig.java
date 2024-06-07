package com.puente.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrfz)->csrfz
                        .disable())
                .cors(cors->cors
                        .disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((authz)->authz
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/valores/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/log").hasRole("ADMIN")
                                .requestMatchers("/consulta/wsdl05Test").permitAll()
                               // .requestMatchers(HttpMethod.GET, "/consulta/wsdl05Test").hasRole("ADMIN")
                                //.requestMatchers(HttpMethod.PUT).denyAll()
                                //.requestMatchers("/log").hasAuthority("permise_special")
                                //.requestMatchers(HttpMethod.PUT).hasRole("ADMIN")
                        .anyRequest()
                        .authenticated()
                        )
                //.httpBasic(Customizer.withDefaults());
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public UserDetailsService memoryUsers(){
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN").
//                build();
//        UserDetails customer = User.builder()
//                .username("customer")
//                .password(passwordEncoder().encode("customer123"))
//                .roles("CUSTOMER").
//                build();
//        return new InMemoryUserDetailsManager(admin, customer);
//    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
