package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.dto.RegistrationResponseDto;
import ru.job4j.urlshortcut.model.Site;
import ru.job4j.urlshortcut.repository.SiteRepository;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@AllArgsConstructor
public class SiteService {

    private static final int CREDENTIAL_BYTES = 18;

    private static final String LOGIN_PREFIX = "site_";

    private static final SecureRandom RANDOM = new SecureRandom();

    private final SiteRepository siteRepository;

    private final PasswordEncoder passwordEncoder;

    public RegistrationResponseDto register(String domain) {
        var normalizedDomain = domain.trim();
        var plainPassword = generateCredential();
        var site = new Site();
        site.setDomain(normalizedDomain);
        site.setLogin(LOGIN_PREFIX + generateCredential());
        site.setPassword(passwordEncoder.encode(plainPassword));
        try {
            siteRepository.saveAndFlush(site);
            return new RegistrationResponseDto(true, site.getLogin(), plainPassword);
        } catch (DataIntegrityViolationException exception) {
            return new RegistrationResponseDto(false, null, null);
        }
    }

    private String generateCredential() {
        var bytes = new byte[CREDENTIAL_BYTES];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
