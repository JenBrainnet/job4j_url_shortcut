package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.dto.ConvertResponseDto;
import ru.job4j.urlshortcut.model.ShortcutUrl;
import ru.job4j.urlshortcut.repository.ShortcutUrlRepository;
import ru.job4j.urlshortcut.repository.SiteRepository;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShortcutUrlService {

    private static final int CODE_BYTES = 6;

    private static final SecureRandom RANDOM = new SecureRandom();

    private final SiteRepository siteRepository;

    private final ShortcutUrlRepository shortcutUrlRepository;

    @Transactional
    public Optional<ConvertResponseDto> convert(Long siteId, String originalUrl) {
        var siteOptional = siteRepository.findById(siteId);
        if (siteOptional.isEmpty()) {
            return Optional.empty();
        }
        var site = siteOptional.get();
        var normalizedUrl = originalUrl.trim();
        return Optional.of(shortcutUrlRepository.findBySiteAndOriginalUrl(site, normalizedUrl)
                .map(shortcutUrl -> new ConvertResponseDto(shortcutUrl.getCode()))
                .orElseGet(() -> {
                    var shortcutUrl = new ShortcutUrl();
                    shortcutUrl.setSite(site);
                    shortcutUrl.setOriginalUrl(normalizedUrl);
                    shortcutUrl.setCode(generateUniqueCode());
                    return new ConvertResponseDto(shortcutUrlRepository.save(shortcutUrl).getCode());
                }));
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (shortcutUrlRepository.existsByCode(code));
        return code;
    }

    private String generateCode() {
        var bytes = new byte[CODE_BYTES];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
