package org.radargps.localapplication.data_receiver;

import jakarta.validation.Valid;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceCreateCommand;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceRequest;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceUpdateCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/data-capture")
public class DataCaptureDeviceController {

    private final DataCaptureDeviceService dataCaptureDeviceService;

    public DataCaptureDeviceController(DataCaptureDeviceService dataCaptureDeviceService) {
        this.dataCaptureDeviceService = dataCaptureDeviceService;
    }

    @PostMapping
    public ResponseEntity<DataCaptureDeviceRequest> create(@Valid @RequestBody DataCaptureDeviceCreateCommand command) {
        return ResponseEntity.created(URI.create("")).body(dataCaptureDeviceService.create(command));
    }

    @PatchMapping("{deviceId}")
    public ResponseEntity<DataCaptureDeviceRequest> updatePartially(@PathVariable UUID deviceId, @Valid @RequestBody DataCaptureDeviceUpdateCommand command) {
        return ResponseEntity.ok(dataCaptureDeviceService.partialUpdate(deviceId, command));
    }

    @GetMapping("{deviceId}")
    public ResponseEntity<DataCaptureDeviceRequest> findOne(@PathVariable UUID deviceId) {
        return ResponseEntity.ok(
                dataCaptureDeviceService.findOne(deviceId)
                        .orElseThrow(() -> new RuntimeException("not found"))
        );
    }

    @GetMapping("/unique-id/{uniqueId}")
    public ResponseEntity<DataCaptureDeviceRequest> findOne(@PathVariable String uniqueId) {
        return ResponseEntity.ok(
                dataCaptureDeviceService.findByUniqueId(uniqueId)
                        .orElseThrow(() -> new RuntimeException("not found"))
        );
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<DataCaptureDeviceRequest>> findAllByCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(dataCaptureDeviceService.findByCompany(companyId));
    }
}
