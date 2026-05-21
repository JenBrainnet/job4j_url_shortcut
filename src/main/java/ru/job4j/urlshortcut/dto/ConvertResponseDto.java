package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response data for URL conversion")
public class ConvertResponseDto {

    @Schema(description = "Shortcut code", example = "ZRUfdD2")
    private String code;

}
