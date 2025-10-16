package com.example.escola.services;

import com.example.escola.dto.CadastrarSerieRequest;
import com.example.escola.model.Professores;
import com.example.escola.model.Series;
import com.example.escola.repository.ProfessoresRepository;
import com.example.escola.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private ProfessoresRepository professoresRepository;

    public Series cadastrarSerie(CadastrarSerieRequest request) {
        Series serie = new Series();
        serie.setNome(request.getNome());
        serie.setAno(request.getAno());

        Set<Professores> professores = professoresRepository.findAllById(request.getProfessoresIds())
                .stream().collect(Collectors.toSet());
        if (professores.isEmpty()) {
            throw new IllegalArgumentException("Nenhum professor válido foi encontrado.");
        }
        serie.setProfessores(professores);

        return seriesRepository.save(serie);
    }
}