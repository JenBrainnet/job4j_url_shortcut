package ru.job4j.urlshortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.job4j.urlshortcut.dto.RegistrationRequestDto;
import ru.job4j.urlshortcut.dto.RegistrationResponseDto;

@Tag(name = "RegistrationController", description = "Site registration APIs")
public interface RegistrationApi {

    @Operation(summary = "Register a site", description = "Registers a site and returns generated login and password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration result was returned",
                    content = @Content(schema = @Schema(implementation = RegistrationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Request body validation failed", content = @Content)
    })
    ResponseEntity<RegistrationResponseDto> register(RegistrationRequestDto request);

}
