package ru.job4j.urlshortcut.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConvertRequestDto {

    @NotBlank(message = "url must not be blank")
    private String url;

}
