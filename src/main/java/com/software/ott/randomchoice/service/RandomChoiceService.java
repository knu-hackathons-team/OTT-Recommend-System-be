package com.software.ott.randomchoice.service;

import com.software.ott.common.exception.BadRequestException;
import com.software.ott.content.entity.Content;
import com.software.ott.content.repository.ContentRepository;
import com.software.ott.randomchoice.dto.RandomContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RandomChoiceService {

    private final ContentRepository contentRepository;
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<RandomContentResponse> getRandomContentList(int count) {
        long totalCount = contentRepository.count();

        if (count <= 0 || count >= totalCount) {
            throw new BadRequestException("count를 0이하 %d이상으로 설정할 수 없습니다.".formatted(totalCount));
        }

        List<Long> randomIds = new ArrayList<>();
        while (randomIds.size() < count) {
            long randomId = 1L + random.nextInt((int) totalCount);
            if (!randomIds.contains(randomId)) {
                randomIds.add(randomId);
            }
        }

        List<Content> randomContents = randomIds.stream()
                .map(contentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return randomContents.stream()
                .map(RandomContentResponse::new)
                .collect(Collectors.toList());
    }
}
