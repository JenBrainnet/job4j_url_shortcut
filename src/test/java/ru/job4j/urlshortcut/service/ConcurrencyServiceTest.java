package ru.job4j.urlshortcut.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.urlshortcut.dto.ConvertResponseDto;
import ru.job4j.urlshortcut.dto.RegistrationResponseDto;
import ru.job4j.urlshortcut.repository.ShortcutUrlRepository;
import ru.job4j.urlshortcut.repository.SiteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrencyServiceTest {

    private static final String DOMAIN = "job4j.ru";

    private static final String ORIGINAL_URL = "https://job4j.ru/profile/exercise/106/task-view/532";

    @Autowired
    private SiteService siteService;

    @Autowired
    private ShortcutUrlService shortcutUrlService;

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
    void whenRegisterSameDomainConcurrentlyThenOnlyOneSiteCreated() throws Exception {
        var threadCount = 8;

        var responses = runConcurrently(threadCount, () -> siteService.register(DOMAIN));

        assertThat(responses)
                .extracting(RegistrationResponseDto::isRegistration)
                .containsOnlyOnce(true);
        assertThat(siteRepository.findAll()).hasSize(1);
        assertThat(siteRepository.findByDomain(DOMAIN)).isPresent();
    }

    @Test
    void whenConvertSameUrlConcurrentlyThenOnlyOneShortcutUrlCreated() throws Exception {
        var threadCount = 8;
        var site = registerSite();

        var responses = runConcurrently(threadCount, () -> shortcutUrlService.convert(site.getId(), ORIGINAL_URL));

        assertThat(responses).allSatisfy(response -> assertThat(response).isPresent());
        assertThat(responses.stream()
                .map(Optional::orElseThrow)
                .map(ConvertResponseDto::getCode)
                .distinct()
                .toList()).hasSize(1);
        assertThat(shortcutUrlRepository.findAll()).hasSize(1);
    }

    @Test
    void whenRedirectSameCodeConcurrentlyThenTotalMatchesRequestCount() throws Exception {
        var threadCount = 20;
        var site = registerSite();
        var code = shortcutUrlService.convert(site.getId(), ORIGINAL_URL).orElseThrow().getCode();

        var responses = runConcurrently(threadCount,
                () -> shortcutUrlService.getOriginalUrlAndIncrementTotal(code));

        assertThat(responses).allSatisfy(response -> assertThat(response).contains(ORIGINAL_URL));
        assertThat(shortcutUrlRepository.findByCode(code))
                .hasValueSatisfying(shortcutUrl -> assertThat(shortcutUrl.getTotal()).isEqualTo(threadCount));
    }

    private ru.job4j.urlshortcut.model.Site registerSite() {
        var registration = siteService.register(DOMAIN);
        return siteRepository.findByLogin(registration.getLogin()).orElseThrow();
    }

    private <T> List<T> runConcurrently(int threadCount, Callable<T> task) throws Exception {
        var executor = Executors.newFixedThreadPool(threadCount);
        var start = new CountDownLatch(1);
        var futures = IntStream.range(0, threadCount)
                .mapToObj(index -> executor.submit(() -> {
                    start.await();
                    return task.call();
                }))
                .toList();
        start.countDown();

        try {
            var responses = new ArrayList<T>();
            for (var future : futures) {
                responses.add(future.get(10, TimeUnit.SECONDS));
            }
            return responses;
        } finally {
            executor.shutdownNow();
        }
    }

}
