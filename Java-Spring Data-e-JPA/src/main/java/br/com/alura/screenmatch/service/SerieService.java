package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Regra de negocio
@Service
public class SerieService {
    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> Obterseries() {
        return converteDados(repository.findAll());
    }

    public List<SerieDTO> top5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> lancamentos(){
        return converteDados(repository.findTop5ByOrderByEpisodiosDataLancamentoDesc());
    }

    private List<SerieDTO> converteDados(List<Serie> serieList) {
        return serieList.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    public SerieDTO obterSeriePorId(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse());
        }

        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream().map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio())).collect(Collectors.toList());
        }

        return null;
    }

    public List<EpisodioDTO> obterEpisodiosPorTemporada(Long id, Long numero) {
        return repository.episodiosPorTemporada(id, numero).stream().map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio())).collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriePorCategoria(String nomeCategoria) {
        Categoria categoria = Categoria.fromStringPt(nomeCategoria);
        return converteDados(repository.findByGenero(categoria));
    }

    public void editarSerie(SerieDTO dto) {
        if (dto.id() == null) throw new IllegalArgumentException("ID obrigatório.");

        Optional<Serie> serieOptional = repository.findById(dto.id());

        if (serieOptional.isPresent()) {
            Serie serie = serieOptional.get();

            serie.setTitulo(dto.titulo());
            serie.setTotalTemporadas(dto.totalTemporadas());
            serie.setAvaliacao(dto.avaliacao());
            serie.setGenero(dto.genero());
            serie.setAtores(dto.atores());
            serie.setPoster(dto.poster());
            serie.setSinopse(dto.sinopse());

            repository.save(serie);
        } else {
            throw new RuntimeException("Série com ID " + dto.id() + " não encontrada.");
        }
    }

    public void adicionarSerie(SerieDTO dto) {
        Serie novaSerie = new Serie();
        novaSerie.setTitulo(dto.titulo());
        novaSerie.setTotalTemporadas(dto.totalTemporadas());
        novaSerie.setAvaliacao(dto.avaliacao());
        novaSerie.setGenero(dto.genero());
        novaSerie.setAtores(dto.atores());
        novaSerie.setPoster(dto.poster());
        novaSerie.setSinopse(dto.sinopse());
        repository.save(novaSerie);
    }
}

