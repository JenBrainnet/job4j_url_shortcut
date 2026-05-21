package ru.job4j.urlshortcut.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import ru.job4j.urlshortcut.dto.ConvertRequestDto;
import ru.job4j.urlshortcut.dto.ConvertResponseDto;
import ru.job4j.urlshortcut.dto.StatisticResponseDto;
import ru.job4j.urlshortcut.security.SiteDetails;
import ru.job4j.urlshortcut.service.ShortcutUrlService;

@RestController
@AllArgsConstructor
public class ShortcutUrlController {

    private final ShortcutUrlService shortcutUrlService;

    @PostMapping("/convert")
    public ResponseEntity<ConvertResponseDto> convert(
            @AuthenticationPrincipal SiteDetails currentSite,
            @Valid @RequestBody ConvertRequestDto request
    ) {
        return shortcutUrlService.convert(currentSite.getId(), request.getUrl())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/redirect/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        return shortcutUrlService.getOriginalUrlAndIncrementTotal(code)
                .map(originalUrl -> ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(originalUrl))
                        .<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<StatisticResponseDto>> statistic(
            @AuthenticationPrincipal SiteDetails currentSite
    ) {
        return shortcutUrlService.getStatistics(currentSite.getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
