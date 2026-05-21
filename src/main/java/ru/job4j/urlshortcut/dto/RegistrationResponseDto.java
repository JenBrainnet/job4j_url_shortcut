package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response data for site registration")
public class RegistrationResponseDto {

    @Schema(description = "Registration result. False means the site is already registered", example = "true")
    private boolean registration;

    @Schema(description = "Generated site login", example = "site_aB12Cd34")
    private String login;

    @Schema(description = "Generated site password. Returned only during successful registration", example = "xYz_123456")
    private String password;

}
