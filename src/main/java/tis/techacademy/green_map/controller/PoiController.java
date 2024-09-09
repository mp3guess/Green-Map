package tis.techacademy.green_map.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tis.techacademy.green_map.controller.model.poi.PoiRequest;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.service.poi.PoiService;

@RestController
@RequestMapping("/api/poi")
@RequiredArgsConstructor
@Slf4j
public class PoiController {

    private final PoiService poiService;

    @Operation(summary = "Get paginated POI data")
    @GetMapping("/paginated")
    public ResponseEntity<Page<PoiEntity>> getPaginatedData(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page,
            Pageable pageable) {
        log.info("Get paginated POI data requested");
        Page<PoiEntity> dataPage = poiService.getAllPois(pageable);
        return ResponseEntity.ok(dataPage);
    }

    @Operation(summary = "Add new POI")
    @PostMapping
    public ResponseEntity<PoiEntity> addNewPoi(@Valid @RequestBody PoiRequest poiRequest) {
        log.info("Add new POI requested");
        return new ResponseEntity<>(poiService.addNewPoi(poiRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get POI by ID")
    @GetMapping("/{poiId}")
    public ResponseEntity<PoiEntity> getPoi(@PathVariable Long poiId) {
        log.info("Get POI by ID requested for ID: {}", poiId);
        return ResponseEntity.ok(poiService.findPoiById(poiId));
    }

    @Operation(summary = "Update POI by ID")
    @PutMapping("/{poiId}")
    public ResponseEntity<PoiEntity> updatePoi(@PathVariable Long poiId, @Valid @RequestBody PoiRequest updatedPoiRequest) {
        log.info("Update POI by ID requested for ID: {}", poiId);
        PoiEntity updatedPoi = poiService.updatePoi(poiId, updatedPoiRequest);
        return ResponseEntity.ok(updatedPoi);
    }

    @Operation(summary = "Delete POI by ID")
    @DeleteMapping(path = "/{poiId}")
    public ResponseEntity<String> deletePoi(@PathVariable Long poiId) {
        log.info("Delete POI by ID requested for ID: {}", poiId);
        poiService.deletePoi(poiId);
        return ResponseEntity.ok().build();
    }
}
