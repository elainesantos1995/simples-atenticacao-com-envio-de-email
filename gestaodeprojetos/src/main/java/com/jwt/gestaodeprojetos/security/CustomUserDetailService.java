package com.jwt.gestaodeprojetos.security;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt.gestaodeprojetos.model.Usuario;
import com.jwt.gestaodeprojetos.service.UsuarioService;

@Service
public class CustomUserDetailService implements UserDetailsService{

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = getUser(() -> usuarioService.obterPorEmail(email));
        return usuario;
    }

    public UserDetails obterUsuarioPorId(Long id) {
        return  getUser(() -> usuarioService.obterPorId(id));
    }

    private Usuario getUser(Supplier<Optional<Usuario>> supplier){
        return supplier.get().orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }
    
}
