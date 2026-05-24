package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.dto.ConvertResponseDto;
import ru.job4j.urlshortcut.dto.StatisticResponseDto;
import ru.job4j.urlshortcut.model.ShortcutUrl;
import ru.job4j.urlshortcut.repository.ShortcutUrlRepository;
import ru.job4j.urlshortcut.repository.SiteRepository;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShortcutUrlService {

    private static final int CODE_BYTES = 6;

    private static final SecureRandom RANDOM = new SecureRandom();

    private final SiteRepository siteRepository;

    private final ShortcutUrlRepository shortcutUrlRepository;

    public Optional<ConvertResponseDto> convert(Long siteId, String originalUrl) {
        var siteOptional = siteRepository.findById(siteId);
        if (siteOptional.isEmpty()) {
            return Optional.empty();
        }
        var site = siteOptional.get();
        var normalizedUrl = originalUrl.trim();
        try {
            return Optional.of(shortcutUrlRepository.findBySiteAndOriginalUrl(site, normalizedUrl)
                    .map(shortcutUrl -> new ConvertResponseDto(shortcutUrl.getCode()))
                    .orElseGet(() -> {
                        var shortcutUrl = new ShortcutUrl();
                        shortcutUrl.setSite(site);
                        shortcutUrl.setOriginalUrl(normalizedUrl);
                        shortcutUrl.setCode(generateCode());
                        return new ConvertResponseDto(shortcutUrlRepository.saveAndFlush(shortcutUrl).getCode());
                    }));
        } catch (DataIntegrityViolationException exception) {
            return shortcutUrlRepository.findBySiteAndOriginalUrl(site, normalizedUrl)
                    .map(shortcutUrl -> new ConvertResponseDto(shortcutUrl.getCode()));
        }
    }

    @Transactional
    public Optional<String> getOriginalUrlAndIncrementTotal(String code) {
        var updatedRows = shortcutUrlRepository.incrementTotalByCode(code);
        if (updatedRows == 0) {
            return Optional.empty();
        }
        return shortcutUrlRepository.findByCode(code)
                .map(ShortcutUrl::getOriginalUrl);
    }

    @Transactional(readOnly = true)
    public Optional<List<StatisticResponseDto>> getStatistics(Long siteId) {
        return siteRepository.findById(siteId)
                .map(shortcutUrlRepository::findAllBySite)
                .map(shortcutUrls -> shortcutUrls.stream()
                        .map(shortcutUrl -> new StatisticResponseDto(
                                shortcutUrl.getOriginalUrl(),
                                shortcutUrl.getTotal()
                        ))
                        .toList());
    }

    private String generateCode() {
        var bytes = new byte[CODE_BYTES];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
