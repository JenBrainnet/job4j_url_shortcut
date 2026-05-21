package ru.job4j.urlshortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.urlshortcut.dto.ConvertRequestDto;
import ru.job4j.urlshortcut.model.Site;
import ru.job4j.urlshortcut.repository.ShortcutUrlRepository;
import ru.job4j.urlshortcut.repository.SiteRepository;
import ru.job4j.urlshortcut.service.AuthService;
import ru.job4j.urlshortcut.service.ShortcutUrlService;
import ru.job4j.urlshortcut.service.SiteService;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ShortcutUrlControllerTest {

    private static final String ORIGINAL_URL = "https://job4j.ru/profile/exercise/106/task-view/532";

    private static final String OTHER_URL = "https://job4j.ru/profile/exercise/107/task-view/533";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SiteService siteService;

    @Autowired
    private AuthService authService;

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
    void whenConvertWithoutTokenThenReturnUnauthorized() throws Exception {
        var request = new ConvertRequestDto();
        request.setUrl(ORIGINAL_URL);

        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenConvertWithTokenThenReturnCode() throws Exception {
        var request = new ConvertRequestDto();
        request.setUrl(ORIGINAL_URL);

        mockMvc.perform(post("/convert")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("job4j.ru"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNotEmpty());
    }

    @Test
    void whenRedirectByCodeThenReturnFoundWithLocationHeader() throws Exception {
        var testSite = registerSite("job4j.ru");
        var code = shortcutUrlService.convert(testSite.site().getId(), ORIGINAL_URL).orElseThrow().getCode();

        mockMvc.perform(get("/redirect/{code}", code))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, ORIGINAL_URL));

        mockMvc.perform(get("/statistic")
                        .header(HttpHeaders.AUTHORIZATION, testSite.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value(ORIGINAL_URL))
                .andExpect(jsonPath("$[0].total").value(1));
    }

    @Test
    void whenRedirectByUnknownCodeThenReturnNotFound() throws Exception {
        mockMvc.perform(get("/redirect/{code}", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetStatisticThenReturnCurrentSiteUrlsOnly() throws Exception {
        var firstSite = registerSite("job4j.ru");
        var secondSite = registerSite("example.com");
        shortcutUrlService.convert(firstSite.site().getId(), ORIGINAL_URL);
        shortcutUrlService.convert(firstSite.site().getId(), OTHER_URL);
        shortcutUrlService.convert(secondSite.site().getId(), "https://example.com/page");

        mockMvc.perform(get("/statistic")
                        .header(HttpHeaders.AUTHORIZATION, firstSite.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].url", containsInAnyOrder(ORIGINAL_URL, OTHER_URL)))
                .andExpect(jsonPath("$[*].total", containsInAnyOrder(0, 0)));
    }

    private TestSite registerSite(String domain) {
        var registration = siteService.register(domain);
        var site = siteRepository.findByLogin(registration.getLogin()).orElseThrow();
        var token = authService.login(registration.getLogin(), registration.getPassword()).orElseThrow().getToken();
        return new TestSite(site, "Bearer " + token);
    }

    private String bearerToken(String domain) {
        return registerSite(domain).bearerToken();
    }

    private record TestSite(Site site, String bearerToken) {
    }

}
