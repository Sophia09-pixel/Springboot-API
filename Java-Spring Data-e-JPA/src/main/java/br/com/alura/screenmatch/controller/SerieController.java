package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.MensagemDTO;
import br.com.alura.screenmatch.dto.RespostaMensagemDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ChatService;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    private SerieService serieService;

    @Autowired
    private ChatService chatService;

    @GetMapping
    public List<SerieDTO> series() {
        return serieService.Obterseries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> top5() {
        return serieService.top5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> lancamentos() {
        return serieService.lancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id) {
        return serieService.obterSeriePorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTemporadasTodas(@PathVariable Long id) {
        return serieService.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumeoro(@PathVariable Long id, @PathVariable Long numero){
        return serieService.obterEpisodiosPorTemporada(id, numero);
    }

    @GetMapping("/categoria/{nomeCategoria}")
    public List<SerieDTO> obterSeriePorCategoria(@PathVariable String nomeCategoria) {
        return serieService.obterSeriePorCategoria(nomeCategoria);
    }

    @PostMapping("/enviarMensagemGPT")
    public RespostaMensagemDTO editar(@RequestBody MensagemDTO mensagemDTO) {
        return chatService.enviarMensagem(mensagemDTO.mensgem());
    }

    @PutMapping("/editar")
    public ResponseEntity<String> editarSerie(@RequestBody SerieDTO serieDTO) {
        try {
            serieService.editarSerie(serieDTO);
            return ResponseEntity.ok("Série editada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado ao editar série.");
        }
    }

    @PostMapping("/adicionar")
    public ResponseEntity<String> adiriocarSerie(@RequestBody SerieDTO serieDTO) {
        try {
            serieService.adicionarSerie(serieDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Série adicionada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar série.");
        }
    }
}



