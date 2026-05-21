package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public RegistrationResponseDto register(String domain) {
        var normalizedDomain = domain.trim();
        if (siteRepository.existsByDomain(normalizedDomain)) {
            return new RegistrationResponseDto(false, null, null);
        }
        var plainPassword = generateCredential();
        var site = new Site();
        site.setDomain(normalizedDomain);
        site.setLogin(generateUniqueLogin());
        site.setPassword(passwordEncoder.encode(plainPassword));
        siteRepository.save(site);
        return new RegistrationResponseDto(true, site.getLogin(), plainPassword);
    }

    private String generateUniqueLogin() {
        String login;
        do {
            login = LOGIN_PREFIX + generateCredential();
        } while (siteRepository.existsByLogin(login));
        return login;
    }

    private String generateCredential() {
        var bytes = new byte[CREDENTIAL_BYTES];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
