package ru.job4j.urlshortcut.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.urlshortcut.repository.ShortcutUrlRepository;
import ru.job4j.urlshortcut.repository.SiteRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SiteServiceTest {

    @Autowired
    private SiteService siteService;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ShortcutUrlRepository shortcutUrlRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        shortcutUrlRepository.deleteAll();
        siteRepository.deleteAll();
    }

    @Test
    void whenRegisterNewDomainThenReturnCredentialsAndSaveSite() {
        var response = siteService.register("job4j.ru");
        var savedSite = siteRepository.findByDomain("job4j.ru");

        assertThat(response.isRegistration()).isTrue();
        assertThat(response.getLogin()).isNotBlank();
        assertThat(response.getPassword()).isNotBlank();
        assertThat(savedSite).isPresent();
        assertThat(savedSite.get().getLogin()).isEqualTo(response.getLogin());
        assertThat(passwordEncoder.matches(response.getPassword(), savedSite.get().getPassword())).isTrue();
    }

    @Test
    void whenRegisterExistingDomainThenReturnFalseWithoutCredentials() {
        siteService.register("job4j.ru");
        var response = siteService.register("job4j.ru");

        assertThat(response.isRegistration()).isFalse();
        assertThat(response.getLogin()).isNull();
        assertThat(response.getPassword()).isNull();
        assertThat(siteRepository.findAll()).hasSize(1);
    }

    @Test
    void whenRegisterNewDomainThenPasswordIsNotStoredAsPlainText() {
        var response = siteService.register("job4j.ru");
        var savedSite = siteRepository.findByLogin(response.getLogin()).orElseThrow();

        assertThat(savedSite.getPassword()).isNotEqualTo(response.getPassword());
    }

}
