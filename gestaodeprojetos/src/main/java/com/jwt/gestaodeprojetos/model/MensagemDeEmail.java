package com.jwt.gestaodeprojetos.model;

import java.util.List;

public class MensagemDeEmail {
    
    private List<String> destinatarios;

    private String remetente;

    private String assunto;

    private String mensagem;

    public MensagemDeEmail(List<String> destinatarios, String remetente, String assunto, String mensagem) {
        this.destinatarios = destinatarios;
        this.remetente = remetente;
        this.assunto = assunto;
        this.mensagem = mensagem;
    }

    public List<String> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatario(List<String> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    

}
