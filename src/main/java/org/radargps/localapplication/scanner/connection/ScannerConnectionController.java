package org.radargps.localapplication.scanner.connection;

import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionCreateCommand;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionRequest;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionUpdateCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/scanner-connection")
public class ScannerConnectionController {
    private final ScannerConnectionService scannerConnectionService;

    public ScannerConnectionController(ScannerConnectionService scannerConnectionService) {
        this.scannerConnectionService = scannerConnectionService;
    }

    @PostMapping
    public ResponseEntity<ScannerConnectionRequest> createConnection(@RequestBody ScannerConnectionCreateCommand command) {
        return ResponseEntity.ok(this.scannerConnectionService.createConnection(command));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScannerConnectionRequest> updateConnection(@PathVariable UUID id, @RequestBody ScannerConnectionUpdateCommand updateCommand) {
        return ResponseEntity.ok(this.scannerConnectionService.partialUpdate(id, updateCommand));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScannerConnectionRequest> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.scannerConnectionService.findById(id)
                .orElseThrow(ResourceNotFoundException::new));
    }

    @GetMapping
    public ResponseEntity<Page<ScannerConnectionRequest>> findAll(@RequestParam(required = false) UUID companyId,
                                                                  @RequestParam(required = false) ScannerConnectionType type,
                                                                  @RequestParam(required = false) Integer pageSize,
                                                                  @RequestParam(required = false) Integer pageNumber) {
        return ResponseEntity.ok(this.scannerConnectionService.findAll(companyId, type, pageSize, pageNumber));
    }
}
