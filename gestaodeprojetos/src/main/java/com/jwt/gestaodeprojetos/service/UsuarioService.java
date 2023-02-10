package com.jwt.gestaodeprojetos.service;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.gestaodeprojetos.model.Usuario;
import com.jwt.gestaodeprojetos.repository.UsuarioRepository;
import com.jwt.gestaodeprojetos.security.JWTService;
import com.jwt.gestaodeprojetos.view.model.usuario.LoginResponse;

@Service
public class UsuarioService {

    private static final String headerPrefix= "Bearer ";
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Usuario> obterTodos(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obterPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obterPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    public Usuario adicionar(Usuario usuario){

        usuario.setId(null);

        if(obterPorEmail(usuario.getEmail()).isPresent()){
            throw new InputMismatchException("Já existe um usuário com o e-mail cadastrado: " + 
            usuario.getEmail());
        }

        // codificando a senha para que ela deixe de ser pública, gerando um hash
        String senha = passwordEncoder.encode(usuario.getSenha());

        usuario.setSenha(senha);

        return usuarioRepository.save(usuario);
    }

    public LoginResponse login(String email, String senha){

        Authentication autenticacao = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, senha, Collections.emptyList()));

        SecurityContextHolder.getContext().setAuthentication(autenticacao);

        String token = headerPrefix + jwtService.gerarToken(autenticacao);

        Usuario usuario = usuarioRepository.findByEmail(email).get();

        return new LoginResponse(usuario, token);
    }
}
