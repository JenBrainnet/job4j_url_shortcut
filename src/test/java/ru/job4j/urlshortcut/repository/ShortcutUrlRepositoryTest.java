package ru.job4j.urlshortcut.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.urlshortcut.model.ShortcutUrl;
import ru.job4j.urlshortcut.model.Site;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ShortcutUrlRepositoryTest {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ShortcutUrlRepository shortcutUrlRepository;

    /**
     * Checks that a shortcut URL can be found by the site that owns it
     * and the original URL submitted for conversion.
     */
    @Test
    void whenFindBySiteAndOriginalUrlThenGetSavedShortcutUrl() {
        Site site = new Site();
        site.setDomain("job4j.ru");
        site.setLogin("login");
        site.setPassword("password");
        Site savedSite = siteRepository.save(site);

        ShortcutUrl shortcutUrl = new ShortcutUrl();
        shortcutUrl.setOriginalUrl("https://job4j.ru/profile/exercise/106/task-view/532");
        shortcutUrl.setCode("ZRUfdD2");
        shortcutUrl.setSite(savedSite);
        shortcutUrlRepository.save(shortcutUrl);

        assertThat(shortcutUrlRepository.findBySiteAndOriginalUrl(
                savedSite,
                "https://job4j.ru/profile/exercise/106/task-view/532"
        )).hasValueSatisfying(foundUrl -> assertThat(foundUrl.getCode()).isEqualTo("ZRUfdD2"));
    }

}
