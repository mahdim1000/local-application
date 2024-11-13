package org.radargps.localapplication.scanner.connection;

import jakarta.validation.Valid;
import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionCreateCommand;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionRequest;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionUpdateCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/scanner-connection")
@Validated
public class ScannerConnectionController {
    private final ScannerConnectionService scannerConnectionService;

    public ScannerConnectionController(ScannerConnectionService scannerConnectionService) {
        this.scannerConnectionService = scannerConnectionService;
    }

    @PostMapping
    public ResponseEntity<ScannerConnectionRequest> createConnection(@Valid @RequestBody ScannerConnectionCreateCommand command) {
        ScannerConnectionRequest connection = this.scannerConnectionService.createConnection(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(connection);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScannerConnectionRequest> updateConnection(
            @PathVariable UUID id,
            @Valid @RequestBody ScannerConnectionUpdateCommand updateCommand) {
        return ResponseEntity.ok(this.scannerConnectionService.partialUpdate(id, updateCommand));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScannerConnectionRequest> findById(@PathVariable UUID id) {
        return this.scannerConnectionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping
    public ResponseEntity<Page<ScannerConnectionRequest>> findAll(
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) ScannerConnectionType type,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber) {

        Page<ScannerConnectionRequest> results = this.scannerConnectionService.findAll(companyId, type, pageSize, pageNumber);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(results.getTotalElements()))
                .body(results);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConnection(@PathVariable UUID id) {
        this.scannerConnectionService.deleteConnection(id);
    }
}
