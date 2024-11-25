package com.software.ott.content.service;

import com.software.ott.content.entity.Content;
import com.software.ott.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvContentService {

    private final ContentRepository contentRepository;

    @Transactional
    public void saveContentFromCsv() {
        try {
            ClassPathResource resource = new ClassPathResource("content_poster.csv");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );
            String[] headers = {"num", "show_id", "type", "title", "director", "cast", "country", "date_added", "release_year", "rating", "duration", "listed_in", "description", "poster_path"};
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(headers));

            List<Content> contents = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                Content content = new Content();

                content.setShowId(record.get("show_id"));
                content.setType(record.get("type"));
                content.setTitle(record.get("title"));
                content.setDirector(record.get("director"));
                content.setCast(record.get("cast"));
                content.setCountry(record.get("country"));
                content.setDateAdded(record.get("date_added"));
                content.setReleaseYear(record.get("release_year"));
                content.setRating(record.get("rating"));
                content.setDuration(record.get("duration"));
                content.setListedIn(record.get("listed_in"));
                content.setDescription(record.get("description"));
                content.setPosterPath(record.get("poster_path"));

                contents.add(content);
            }

            contentRepository.saveAll(contents);

            System.out.println("Total records in CSV: " + contents.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
