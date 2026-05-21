package ru.job4j.urlshortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.job4j.urlshortcut.dto.LoginRequestDto;
import ru.job4j.urlshortcut.dto.LoginResponseDto;
import ru.job4j.urlshortcut.validation.ValidationErrorResponse;

@Tag(name = "AuthController", description = "Authentication APIs")
public interface AuthApi {

    @Operation(summary = "Authenticate a site", description = "Authenticates a site and returns JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Site was authenticated",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Request body validation failed",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Bad credentials", content = @Content)
    })
    ResponseEntity<LoginResponseDto> login(LoginRequestDto request);

}
