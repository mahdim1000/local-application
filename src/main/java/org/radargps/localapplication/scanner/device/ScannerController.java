package org.radargps.localapplication.scanner.device;

import jakarta.validation.Valid;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.temp.ProductPalletScannerConnectionCommand;
import org.radargps.localapplication.scanner.dto.ScannerCreateCommand;
import org.radargps.localapplication.scanner.dto.ScannerRequest;
import org.radargps.localapplication.scanner.dto.ScannerUpdateCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/data-capture")
public class ScannerController {

    private final ScannerService scannerService;

    public ScannerController(ScannerService scannerService) {
        this.scannerService = scannerService;
    }

    @PostMapping
    public ResponseEntity<ScannerRequest> create(@Valid @RequestBody ScannerCreateCommand command) {
        return ResponseEntity.created(URI.create("")).body(scannerService.create(command));
    }

    @PatchMapping("{deviceId}")
    public ResponseEntity<ScannerRequest> updatePartially(@PathVariable UUID deviceId, @Valid @RequestBody ScannerUpdateCommand command) {
        return ResponseEntity.ok(scannerService.partialUpdate(deviceId, command));
    }

    @GetMapping("{deviceId}")
    public ResponseEntity<ScannerRequest> findById(@PathVariable UUID deviceId) {
        return ResponseEntity.ok(
                scannerService.findOne(deviceId)
                        .orElseThrow(() -> new RuntimeException("not found"))
        );
    }

    @GetMapping("/unique-id/{uniqueId}")
    public ResponseEntity<ScannerRequest> findByUniqueId(@PathVariable String uniqueId) {
        return ResponseEntity.ok(
                scannerService.findByUniqueId(uniqueId)
                        .orElseThrow(() -> new RuntimeException("not found"))
        );
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<ScannerRequest>> findAllByCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(scannerService.findByCompany(companyId));
    }

    @PostMapping("connection/product-pallet")
    public ResponseEntity<Void> connect(@RequestBody ProductPalletScannerConnectionCommand command) {
        scannerService.connectToDevice(command);
        return ResponseEntity.ok().build();
    }
}