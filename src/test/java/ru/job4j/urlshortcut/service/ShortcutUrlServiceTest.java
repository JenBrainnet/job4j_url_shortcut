package ru.job4j.urlshortcut.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.urlshortcut.repository.ShortcutUrlRepository;
import ru.job4j.urlshortcut.repository.SiteRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ShortcutUrlServiceTest {

    private static final String ORIGINAL_URL = "https://job4j.ru/profile/exercise/106/task-view/532";

    @Autowired
    private ShortcutUrlService shortcutUrlService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ShortcutUrlRepository shortcutUrlRepository;

    @BeforeEach
    void setUp() {
        shortcutUrlRepository.deleteAll();
        siteRepository.deleteAll();
    }

    @Test
    void whenConvertNewUrlThenReturnCodeAndSaveShortcutUrl() {
        var site = registerSite("job4j.ru");
        var response = shortcutUrlService.convert(site.getId(), ORIGINAL_URL);

        assertThat(response).isPresent();
        assertThat(response.get().getCode()).isNotBlank();
        assertThat(shortcutUrlRepository.findBySiteAndOriginalUrl(site, ORIGINAL_URL))
                .hasValueSatisfying(shortcutUrl -> assertThat(shortcutUrl.getCode()).isEqualTo(response.get().getCode()));
    }

    @Test
    void whenConvertSameUrlForSameSiteThenReturnExistingCode() {
        var site = registerSite("job4j.ru");
        var firstResponse = shortcutUrlService.convert(site.getId(), ORIGINAL_URL).orElseThrow();
        var secondResponse = shortcutUrlService.convert(site.getId(), ORIGINAL_URL).orElseThrow();

        assertThat(secondResponse.getCode()).isEqualTo(firstResponse.getCode());
        assertThat(shortcutUrlRepository.findAll()).hasSize(1);
    }

    @Test
    void whenConvertSameUrlForDifferentSitesThenCreateSeparateShortcutUrls() {
        var firstSite = registerSite("job4j.ru");
        var secondSite = registerSite("example.com");

        var firstResponse = shortcutUrlService.convert(firstSite.getId(), ORIGINAL_URL).orElseThrow();
        var secondResponse = shortcutUrlService.convert(secondSite.getId(), ORIGINAL_URL).orElseThrow();

        assertThat(firstResponse.getCode()).isNotEqualTo(secondResponse.getCode());
        assertThat(shortcutUrlRepository.findAll()).hasSize(2);
    }

    @Test
    void whenConvertForUnknownSiteThenReturnEmpty() {
        var response = shortcutUrlService.convert(1L, ORIGINAL_URL);

        assertThat(response).isEmpty();
    }

    @Test
    void whenGetOriginalUrlByCodeThenReturnUrlAndIncrementTotal() {
        var site = registerSite("job4j.ru");
        var code = shortcutUrlService.convert(site.getId(), ORIGINAL_URL).orElseThrow().getCode();

        var response = shortcutUrlService.getOriginalUrlAndIncrementTotal(code);

        assertThat(response).contains(ORIGINAL_URL);
        assertThat(shortcutUrlRepository.findByCode(code))
                .hasValueSatisfying(shortcutUrl -> assertThat(shortcutUrl.getTotal()).isEqualTo(1));
    }

    @Test
    void whenGetOriginalUrlByCodeSeveralTimesThenIncrementTotalSeveralTimes() {
        var site = registerSite("job4j.ru");
        var code = shortcutUrlService.convert(site.getId(), ORIGINAL_URL).orElseThrow().getCode();

        shortcutUrlService.getOriginalUrlAndIncrementTotal(code);
        shortcutUrlService.getOriginalUrlAndIncrementTotal(code);

        assertThat(shortcutUrlRepository.findByCode(code))
                .hasValueSatisfying(shortcutUrl -> assertThat(shortcutUrl.getTotal()).isEqualTo(2));
    }

    @Test
    void whenGetOriginalUrlByUnknownCodeThenReturnEmpty() {
        var response = shortcutUrlService.getOriginalUrlAndIncrementTotal("unknown");

        assertThat(response).isEmpty();
        assertThat(shortcutUrlRepository.findAll()).isEmpty();
    }

    private ru.job4j.urlshortcut.model.Site registerSite(String domain) {
        var registration = siteService.register(domain);
        return siteRepository.findByLogin(registration.getLogin()).orElseThrow();
    }

}
