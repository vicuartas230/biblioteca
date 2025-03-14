package com.egg.biblioteca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SeguridadWeb {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // http.authorizeHttpRequests((authorize) -> authorize
    // .requestMatchers("/css/", "/js/", "/img/").permitAll()
    // .requestMatchers("/autor/registrar", "/autor/registro",
    // "/autor/modificar/**").hasRole("ADMIN")
    // .anyRequest().authenticated())
    // .formLogin((form) -> form
    // .loginPage("/login")
    // .loginProcessingUrl("/logincheck")
    // .usernameParameter("email")
    // .passwordParameter("password")
    // .defaultSuccessUrl("/inicio", true)
    // .permitAll())
    // .logout((logout) -> logout
    // .logoutUrl("/logout")
    // .logoutSuccessUrl("/login")
    // .permitAll())
    // .csrf(csrf -> csrf.disable());
    // return http.build();
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/admin/**", "/autor/registrar", "/editorial/registrar", "/libro/registrar").hasRole("ADMIN")
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/login", "/registrar", "/registro").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/inicio", true)
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
