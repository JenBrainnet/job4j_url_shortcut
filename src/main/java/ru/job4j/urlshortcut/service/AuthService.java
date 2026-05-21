package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.dto.LoginResponseDto;
import ru.job4j.urlshortcut.security.JwtService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public Optional<LoginResponseDto> login(String login, String password) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password)
            );
            return Optional.of(new LoginResponseDto(
                    jwtService.generateToken(authentication.getName()),
                    TOKEN_TYPE
            ));
        } catch (AuthenticationException exception) {
            return Optional.empty();
        }
    }

}
