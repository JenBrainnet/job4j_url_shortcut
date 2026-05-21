package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request data for site registration")
public class RegistrationRequestDto {

    @NotBlank(message = "site must not be blank")
    @Schema(description = "Site domain", example = "job4j.ru")
    private String site;

}
