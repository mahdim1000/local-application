package org.radargps.localapplication.scanner.device;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.common.constants.PageConstants;
import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.device.domain.ScannerType;
import org.radargps.localapplication.scanner.device.dto.ScannerCreateCommand;
import org.radargps.localapplication.scanner.device.dto.ScannerRequest;
import org.radargps.localapplication.scanner.device.dto.ScannerUpdateCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import static org.radargps.localapplication.common.constants.PageConstants.*;

@RestController
@RequestMapping("/api/v1/scanner")
public class ScannerController {

    private final ScannerService scannerService;

    public ScannerController(ScannerService scannerService) {
        this.scannerService = scannerService;
    }

    @PostMapping
    public ResponseEntity<ScannerRequest> create(@Valid @RequestBody ScannerCreateCommand command) {
        return ResponseEntity.created(URI.create("")).body(scannerService.create(command));
    }

    @PatchMapping("{uniqueId}")
    public ResponseEntity<ScannerRequest> updatePartially(@PathVariable String uniqueId, @Valid @RequestBody ScannerUpdateCommand command) {
        return ResponseEntity.ok(scannerService.partialUpdate(uniqueId, command));
    }

    @GetMapping("{deviceId}")
    public ResponseEntity<ScannerRequest> findById(@PathVariable String uniqueId) {
        return ResponseEntity.ok(
                scannerService.findOne(uniqueId)
                        .orElseThrow(ResourceNotFoundException::new)
        );
    }

    @GetMapping("/unique-id/{uniqueId}")
    public ResponseEntity<ScannerRequest> findByUniqueId(@PathVariable String uniqueId) {
        return ResponseEntity.ok(
                scannerService.findByUniqueId(uniqueId)
                        .orElseThrow(ResourceNotFoundException::new)
        );
    }

    /**
     * Retrieve paginated scanner requests with filtering and sorting capabilities
     *
     * @param companyId Required company identifier
     * @param type Optional {@link ScannerType} filter
     * @param sortBy Optional field to sort by (default: createdAt)
     * @param sortDirection Optional sort direction (ASC/DESC, default: DESC)
     * @param page Optional page number (default: 0)
     * @param size Optional page size (default: 20, max: 100)
     * @return Page of scanner requests
     */
    @GetMapping
    public ResponseEntity<Page<ScannerRequest>> getScannerRequests(
            @RequestParam(required = true) @NotNull UUID companyId,
            @RequestParam(required = false) ScannerType type,
            @RequestParam(required = false, defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = DEFAULT_SORT_DIRECTION) String sortDirection,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer page,
            @RequestParam(required = false, defaultValue = "20") @Min(1) @Max(MAX_PAGE_SIZE) Integer size) {

        Page<ScannerRequest> results = scannerService.findAll(
                companyId, type,
                sortBy, sortDirection,
                page, size
        );

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(results.getTotalElements()))
                .body(results);
    }

    /**
     * Delete a scanner request by ID
     *
     * @param uniqueId The Mac Address of the scanner to delete
     * @return ResponseEntity with no content on success
     */
    @DeleteMapping("/{uniqueId}")
    @Operation(summary = "Delete a scanner request",
            description = "Deletes a scanner request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Scanner successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Scanner not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteScanner(
            @Parameter(description = "Scanner ID to delete", required = true)
            @PathVariable @NotNull String uniqueId) {

        scannerService.deleteScanner(uniqueId);
        return ResponseEntity.noContent().build();
    }
}
