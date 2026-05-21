package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request data for authentication")
public class LoginRequestDto {

    @NotBlank(message = "login must not be blank")
    @Schema(description = "Site login received during registration", example = "site_aB12Cd34")
    private String login;

    @NotBlank(message = "password must not be blank")
    @Schema(description = "Site password received during registration", example = "xYz_123456")
    private String password;

}
