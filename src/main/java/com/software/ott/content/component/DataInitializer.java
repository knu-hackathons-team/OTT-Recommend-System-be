package com.software.ott.content.component;

import com.software.ott.content.service.CsvContentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CsvContentService csvContentService;

    @PostConstruct
    public void init() {
        csvContentService.saveContentFromCsv();
    }
}
