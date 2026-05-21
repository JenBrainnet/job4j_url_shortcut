package ru.job4j.urlshortcut.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationRequestDto {

    @NotBlank(message = "site must not be blank")
    private String site;

}
