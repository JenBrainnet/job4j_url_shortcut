package ru.job4j.urlshortcut.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.urlshortcut.repository.ShortcutUrlRepository;
import ru.job4j.urlshortcut.repository.SiteRepository;
import ru.job4j.urlshortcut.security.JwtService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ShortcutUrlRepository shortcutUrlRepository;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        shortcutUrlRepository.deleteAll();
        siteRepository.deleteAll();
    }

    @Test
    void whenLoginWithValidCredentialsThenReturnBearerToken() {
        var registration = siteService.register("job4j.ru");
        var response = authService.login(registration.getLogin(), registration.getPassword());

        assertThat(response).isPresent();
        assertThat(response.get().getType()).isEqualTo("Bearer");
        assertThat(response.get().getToken()).isNotBlank();
        assertThat(jwtService.validateToken(response.get().getToken())).isTrue();
        assertThat(jwtService.getLoginFromToken(response.get().getToken())).isEqualTo(registration.getLogin());
    }

    @Test
    void whenLoginWithWrongPasswordThenReturnEmpty() {
        var registration = siteService.register("job4j.ru");
        var response = authService.login(registration.getLogin(), "wrong-password");

        assertThat(response).isEmpty();
    }

    @Test
    void whenLoginWithUnknownLoginThenReturnEmpty() {
        var response = authService.login("unknown-login", "password");

        assertThat(response).isEmpty();
    }

}
