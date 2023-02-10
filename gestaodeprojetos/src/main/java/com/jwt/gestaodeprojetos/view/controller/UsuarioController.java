package com.jwt.gestaodeprojetos.view.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.gestaodeprojetos.model.Usuario;
import com.jwt.gestaodeprojetos.model.MensagemDeEmail;
import com.jwt.gestaodeprojetos.service.UsuarioService;
import com.jwt.gestaodeprojetos.service.MailService;
import com.jwt.gestaodeprojetos.view.model.usuario.LoginRequest;
import com.jwt.gestaodeprojetos.view.model.usuario.LoginResponse;

import javax.mail.MessagingException;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MailService emailService;

    @GetMapping
    private List<Usuario> obterTodos(){
        return usuarioService.obterTodos();
    }

    @GetMapping("/{id}")
    private Optional<Usuario> obterPorId(@PathVariable Long id){
        return usuarioService.obterPorId(id);
    }

    @PostMapping
    private Usuario cadastrar(@RequestBody Usuario usuario){
        return usuarioService.adicionar(usuario);
    }

    @PostMapping("/login")
    public LoginResponse logar(@RequestBody LoginRequest request){
        return usuarioService.login(request.getEmail(), request.getSenha());
    }

    @PostMapping("/email")
    public String enviarEmail(@RequestBody MensagemDeEmail email) {

        try {
            emailService.enviarMensagem(email);
        } catch (MessagingException me){
            return "Erro ao enviar email";
        }

        return "sucesso!"; 
        
    }

}
