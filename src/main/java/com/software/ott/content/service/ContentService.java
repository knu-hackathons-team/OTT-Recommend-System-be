package com.software.ott.content.service;

import com.software.ott.content.dto.ContentResponse;
import com.software.ott.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public List<ContentResponse> readContentsWithTitle(String title) {
        return contentRepository.findAllByTitleContaining(title)
                .stream().map(
                        ContentResponse::new
                ).collect(Collectors.toList());
    }

    public List<ContentResponse> readContentsWithGenre(String genre) {
        return contentRepository.findAllByListedInContaining(genre)
                .stream().map(
                        ContentResponse::new
                ).collect(Collectors.toList());
    }

    public List<ContentResponse> readContentsWithCast(String cast) {
        return contentRepository.findAllByCastContaining(cast)
                .stream().map(
                        ContentResponse::new
                ).collect(Collectors.toList());
    }
}
