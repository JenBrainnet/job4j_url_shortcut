package ru.job4j.urlshortcut.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.job4j.urlshortcut.model.Site;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SiteDetails implements UserDetails {

    private static final String SITE_ROLE = "ROLE_SITE";

    private final Long id;

    private final String username;

    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public SiteDetails(
            Long id,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static SiteDetails build(Site site) {
        return new SiteDetails(
                site.getId(),
                site.getLogin(),
                site.getPassword(),
                List.of(new SimpleGrantedAuthority(SITE_ROLE))
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SiteDetails that = (SiteDetails) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
