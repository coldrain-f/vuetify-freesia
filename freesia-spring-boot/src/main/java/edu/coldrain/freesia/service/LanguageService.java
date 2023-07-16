package edu.coldrain.freesia.service;

import edu.coldrain.freesia.dto.LanguageDTO;
import edu.coldrain.freesia.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    public List<LanguageDTO.Response> findAll() {
        return languageRepository.findAll()
                .stream()
                .map(l -> LanguageDTO.Response.builder()
                        .id(l.getId())
                        .name(l.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
