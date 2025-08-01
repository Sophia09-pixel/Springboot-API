package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.RespostaMensagemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {


    public RespostaMensagemDTO enviarMensagem(String mensagem) {
        String reposta = ConsultaChatGPT.interacaoComUsuario(mensagem);
        return new RespostaMensagemDTO(reposta);
    }

}
