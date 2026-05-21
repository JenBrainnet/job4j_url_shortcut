package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response data for successful authentication")
public class LoginResponseDto {

    @Schema(description = "JWT token", example = "e25d31c5-db66-4cf2-85d4-8faa8c544ad6")
    private String token;

    @Schema(description = "Token type", example = "Bearer")
    private String type;

}
