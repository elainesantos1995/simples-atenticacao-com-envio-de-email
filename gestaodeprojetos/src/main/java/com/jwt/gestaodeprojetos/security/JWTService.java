package com.jwt.gestaodeprojetos.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.jwt.gestaodeprojetos.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTService {
    // classe responsável por criar o token

    private static final String chavePrivadaJWT = "secretKey";

    public String gerarToken(Authentication autentication){
        int tempoDeExpiracao = 86400000;

        Date dataExpiracao = new Date(new Date().getTime() + tempoDeExpiracao);

        Usuario usuario = (Usuario) autentication.getPrincipal();

        return Jwts.builder()
            .setSubject(usuario.getId().toString())
            .setIssuedAt(dataExpiracao)
            .setExpiration(dataExpiracao)
            .signWith(SignatureAlgorithm.HS512, chavePrivadaJWT)
            .compact();
    }

    /***
     * Método para retornar o id do usuário dono do token
     * @param token
     * @return id do usuário
    */
    public Optional<Long> obterIdDoUsuario(String token){
        try{
            Claims claims = parse(token).getBody();

            // vai retornar o id do usuário de dentro do token se encontrar, caso contrário, retorna null.
            return Optional.ofNullable(Long.parseLong(claims.getSubject()));

        } catch (Exception e){
            return Optional.empty();
        }
    }

    // método que sabe descobrir com base na chave privada, quais as permissoes do usuário
    private Jws<Claims> parse(String token){
        return Jwts.parser().setSigningKey(chavePrivadaJWT).parseClaimsJws(token);
    }
    
}
