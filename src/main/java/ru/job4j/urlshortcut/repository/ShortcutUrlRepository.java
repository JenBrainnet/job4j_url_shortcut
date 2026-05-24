package ru.job4j.urlshortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.job4j.urlshortcut.model.ShortcutUrl;
import ru.job4j.urlshortcut.model.Site;

import java.util.List;
import java.util.Optional;

public interface ShortcutUrlRepository extends JpaRepository<ShortcutUrl, Long> {

    Optional<ShortcutUrl> findByCode(String code);

    Optional<ShortcutUrl> findBySiteAndOriginalUrl(Site site, String originalUrl);

    List<ShortcutUrl> findAllBySite(Site site);

    @Modifying
    @Query(value = """
            UPDATE shortcut_urls
            SET total = total + 1
            WHERE code = :code
            """, nativeQuery = true)
    int incrementTotalByCode(String code);

}
