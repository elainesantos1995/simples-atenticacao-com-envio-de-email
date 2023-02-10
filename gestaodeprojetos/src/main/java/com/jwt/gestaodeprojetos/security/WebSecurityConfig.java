package com.jwt.gestaodeprojetos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // método essencial para configurar o custom com o método de codificar senha
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
            .userDetailsService(customUserDetailService)
            .passwordEncoder(passwordEncoder());
    }

    // método essencial para trabalhar com autenticação no login
    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
    

    // configuração global de acesso e rota das requisições
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable()
            .exceptionHandling()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()

            .antMatchers(HttpMethod.POST, "/api/usuarios", "/api/usuarios/login")
            .permitAll() // urls que todos podem acessar para fazer autenticações

            .anyRequest()
            .authenticated(); // todas as requisições, devem ser autenticadas

            // configuração par que antes de todas as requisições http, o sistema deve utilizar esse filtro 
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }


}
