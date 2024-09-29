package org.radargps.localapplication.scanner.device;

import jakarta.validation.Valid;
import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.temp.ProductPalletScannerConnectionCommand;
import org.radargps.localapplication.scanner.device.dto.ScannerCreateCommand;
import org.radargps.localapplication.scanner.device.dto.ScannerRequest;
import org.radargps.localapplication.scanner.device.dto.ScannerUpdateCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

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

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<ScannerRequest>> findAllByCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(scannerService.findByCompany(companyId));
    }
}
