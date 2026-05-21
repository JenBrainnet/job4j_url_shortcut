package ru.job4j.urlshortcut.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.dto.RegistrationRequestDto;
import ru.job4j.urlshortcut.dto.RegistrationResponseDto;
import ru.job4j.urlshortcut.service.SiteService;

@RestController
@AllArgsConstructor
public class RegistrationController implements RegistrationApi {

    private final SiteService siteService;

    @PostMapping("/registration")
    @Override
    public ResponseEntity<RegistrationResponseDto> register(
            @Valid @RequestBody RegistrationRequestDto request
    ) {
        return ResponseEntity.ok(siteService.register(request.getSite()));
    }

}
