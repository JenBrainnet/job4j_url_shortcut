package ru.job4j.urlshortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.job4j.urlshortcut.dto.ConvertRequestDto;
import ru.job4j.urlshortcut.dto.ConvertResponseDto;
import ru.job4j.urlshortcut.dto.StatisticResponseDto;
import ru.job4j.urlshortcut.security.SiteDetails;
import ru.job4j.urlshortcut.validation.ValidationErrorResponse;

import java.util.List;

@Tag(name = "ShortcutUrlController", description = "URL shortcut APIs")
public interface ShortcutUrlApi {

    @Operation(summary = "Convert URL", description = "Creates or returns a shortcut code for authenticated site URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "URL was converted",
                    content = @Content(schema = @Schema(implementation = ConvertResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Request body validation failed",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "JWT token is missing or invalid", content = @Content),
            @ApiResponse(responseCode = "404", description = "Authenticated site was not found", content = @Content)
    })
    ResponseEntity<ConvertResponseDto> convert(
            @Parameter(hidden = true) SiteDetails currentSite,
            ConvertRequestDto request
    );

    @Operation(summary = "Redirect by code", description = "Redirects to the original URL and increments redirect counter.")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirect to original URL", content = @Content),
            @ApiResponse(responseCode = "404", description = "Shortcut code was not found", content = @Content)
    })
    ResponseEntity<Void> redirect(String code);

    @Operation(summary = "Retrieve URL statistics", description = "Returns URLs and redirect counts for authenticated site.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statistics were found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = StatisticResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "JWT token is missing or invalid", content = @Content),
            @ApiResponse(responseCode = "404", description = "Authenticated site was not found", content = @Content)
    })
    ResponseEntity<List<StatisticResponseDto>> statistic(@Parameter(hidden = true) SiteDetails currentSite);

}
