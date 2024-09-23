package org.radargps.localapplication.scanner.connection;

import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionCreateCommand;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionRequest;
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
    public ResponseEntity<ScannerConnectionRequest> createConnection(ScannerConnectionCreateCommand command) {
        return ResponseEntity.ok(this.scannerConnectionService.createConnection(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScannerConnectionRequest> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.scannerConnectionService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<Page<ScannerConnectionRequest>> findByCompanyId(@RequestParam UUID companyId,
                                                                         @RequestParam ScannerConnectionType type) {
        return ResponseEntity.ok(this.scannerConnectionService.findByCompanyIdAndType(companyId, type));
    }
}
