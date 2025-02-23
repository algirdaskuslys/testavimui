package com.spring.calculator.config;

import com.spring.calculator.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    @Qualifier("UserDetailsService")
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    @Qualifier("BCryptPasswordEncoder")
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Qualifier("authentication")
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return authProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        auth -> auth.requestMatchers("/register", "/login").permitAll()
                                .anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/login")
                        .loginProcessingUrl("/loginUser")
                        .defaultSuccessUrl("/calculator", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll());
        return http.build();
    }
}
