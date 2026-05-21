package ru.job4j.urlshortcut.validation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "Validation error response")
public class ValidationErrorResponse {

    @Schema(description = "Validation violations")
    private final List<Violation> violations;

}
