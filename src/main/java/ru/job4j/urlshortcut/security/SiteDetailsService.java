package ru.job4j.urlshortcut.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.repository.SiteRepository;

@Service
@AllArgsConstructor
public class SiteDetailsService implements UserDetailsService {

    private final SiteRepository siteRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return siteRepository.findByLogin(login)
                .map(SiteDetails::build)
                .orElseThrow(() -> new UsernameNotFoundException("Site not found with login: " + login));
    }

}
