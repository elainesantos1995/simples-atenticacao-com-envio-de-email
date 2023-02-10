package com.jwt.gestaodeprojetos.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    // método principal no qual toda requisição bate antes de chegar ao endpoint
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, javax.servlet.FilterChain filterChain)
            throws ServletException, IOException {
        
            // pega o token de dentro da requisição
            String token = obterToken(request);

            // pegar o id do usuário de dentro do token
            Optional<Long> idUsuario = jwtService.obterIdDoUsuario(token);

            if(idUsuario.isPresent()){                
            
                UserDetails usuario = customUserDetailService.obterUsuarioPorId(idUsuario.get());

                // verificação se o usuario está autenticado ou não
                // aqui também é possível validar as permissões
                UsernamePasswordAuthenticationToken autotenticacao = 
                    new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList());

                // mudando a autenticação para a própria requisição
                autotenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // setando a autenticação no contexto do security para que o spring gerencie
                SecurityContextHolder.getContext().setAuthentication(autotenticacao);
            }

            // método padrão para requests de usuário
            filterChain.doFilter(request, response);
        
    }

    private String obterToken(HttpServletRequest request){

        String token = request.getHeader("Authorization");

        if(!StringUtils.hasText(token)){
            return null;
        }

        // Pega o conteúdo do token a partir a palavra Bearer do Authorization da request
        return token.substring(7);
    }
    
}
