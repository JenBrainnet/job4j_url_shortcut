package ru.job4j.urlshortcut.validation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "Validation violation data")
public class Violation {

    @Schema(description = "Invalid field name", example = "url")
    private final String fieldName;

    @Schema(description = "Validation error message", example = "url must not be blank")
    private final String message;

}
