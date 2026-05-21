package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "URL redirect statistics")
public class StatisticResponseDto {

    @Schema(description = "Original URL", example = "https://job4j.ru/profile/exercise/106/task-view/532")
    private String url;

    @Schema(description = "Redirect count", example = "103")
    private long total;

}
