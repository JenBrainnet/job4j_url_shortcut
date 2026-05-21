package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request data for URL conversion")
public class ConvertRequestDto {

    @NotBlank(message = "url must not be blank")
    @Schema(description = "Original URL", example = "https://job4j.ru/profile/exercise/106/task-view/532")
    private String url;

}
