package ru.job4j.urlshortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.urlshortcut.model.Site;

import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {

    Optional<Site> findByDomain(String domain);

    Optional<Site> findByLogin(String login);

}
