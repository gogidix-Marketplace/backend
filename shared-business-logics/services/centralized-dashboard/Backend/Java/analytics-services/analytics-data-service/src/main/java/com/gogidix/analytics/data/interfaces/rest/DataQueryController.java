package com.gogidix.analytics.data.interfaces.rest;

import com.gogidix.analytics.data.application.service.DataExportCommandService;
import com.gogidix.analytics.data.application.service.DataQueryCommandService;
import com.gogidix.analytics.data.application.service.DataQueryService;
import com.gogidix.analytics.data.domain.model.DataExport;
import com.gogidix.analytics.data.domain.model.DataQuery;
import com.gogidix.analytics.data.domain.port.in.CreateDataQueryCommand;
import com.gogidix.analytics.data.domain.port.in.CreateExportCommand;
import com.gogidix.analytics.data.domain.port.in.ExecuteDataQueryCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Data Query API.
 */
@RestController
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
@Tag(name = "Data Queries", description = "Data query and export API")
public class DataQueryController {

    private final DataQueryCommandService commandService;
    private final DataExportCommandService exportCommandService;
    private final DataQueryService queryService;

    // Query endpoints

    @PostMapping("/queries")
    @Operation(summary = "Create a saved data query")
    public ResponseEntity<DataQuery> createQuery(@Valid @RequestBody CreateDataQueryCommand command) {
        DataQuery query = commandService.createQuery(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(query);
    }

    @GetMapping("/queries")
    @Operation(summary = "Get all data queries")
    public ResponseEntity<Page<DataQuery>> getQueries(Pageable pageable) {
        return ResponseEntity.ok(queryService.getQueries(pageable));
    }

    @GetMapping("/queries/{queryId}")
    @Operation(summary = "Get query by ID")
    public ResponseEntity<DataQuery> getQuery(@PathVariable String queryId) {
        return ResponseEntity.ok(queryService.getQuery(queryId));
    }

    @GetMapping("/queries/search")
    @Operation(summary = "Search queries")
    public ResponseEntity<List<DataQuery>> searchQueries(@RequestParam String search) {
        return ResponseEntity.ok(queryService.searchQueries(search));
    }

    @GetMapping("/queries/favorites")
    @Operation(summary = "Get favorite queries")
    public ResponseEntity<List<DataQuery>> getFavoriteQueries() {
        return ResponseEntity.ok(queryService.getFavoriteQueries());
    }

    @GetMapping("/queries/public")
    @Operation(summary = "Get public queries")
    public ResponseEntity<List<DataQuery>> getPublicQueries() {
        return ResponseEntity.ok(queryService.getPublicQueries());
    }

    @PutMapping("/queries/{queryId}")
    @Operation(summary = "Update a data query")
    public ResponseEntity<DataQuery> updateQuery(
        @PathVariable String queryId,
        @Valid @RequestBody CreateDataQueryCommand command) {

        return ResponseEntity.ok(commandService.updateQuery(queryId, command));
    }

    @DeleteMapping("/queries/{queryId}")
    @Operation(summary = "Delete a data query")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuery(@PathVariable String queryId) {
        commandService.deleteQuery(queryId);
    }

    @PutMapping("/queries/{queryId}/favorite")
    @Operation(summary = "Toggle favorite status")
    public ResponseEntity<DataQuery> toggleFavorite(@PathVariable String queryId) {
        return ResponseEntity.ok(commandService.toggleFavorite(queryId));
    }

    @PostMapping("/execute")
    @Operation(summary = "Execute a data query")
    public ResponseEntity<Map<String, Object>> executeQuery(@Valid @RequestBody ExecuteDataQueryCommand command) {
        return ResponseEntity.ok(queryService.executeQuery(command));
    }

    @PostMapping("/queries/{queryId}/execute")
    @Operation(summary = "Execute a saved data query")
    public ResponseEntity<Map<String, Object>> executeSavedQuery(
        @PathVariable String queryId,
        @RequestBody(required = false) Map<String, Object> parameters) {

        ExecuteDataQueryCommand command = ExecuteDataQueryCommand.builder()
            .queryId(queryId)
            .parameters(parameters)
            .build();

        return ResponseEntity.ok(queryService.executeQuery(command));
    }

    @GetMapping("/queries/schema")
    @Operation(summary = "Get query schema")
    public ResponseEntity<Map<String, Object>> getQuerySchema(@RequestParam String queryDefinition) {
        return ResponseEntity.ok(queryService.getQuerySchema(queryDefinition));
    }

    // Export endpoints

    @PostMapping("/exports")
    @Operation(summary = "Create a data export")
    public ResponseEntity<DataExport> createExport(@Valid @RequestBody CreateExportCommand command) {
        DataExport export = exportCommandService.createExport(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(export);
    }

    @GetMapping("/exports")
    @Operation(summary = "Get all exports")
    public ResponseEntity<Page<DataExport>> getExports(Pageable pageable) {
        return ResponseEntity.ok(queryService.getExports(pageable));
    }

    @GetMapping("/exports/{exportId}")
    @Operation(summary = "Get export by ID")
    public ResponseEntity<DataExport> getExport(@PathVariable String exportId) {
        return ResponseEntity.ok(queryService.getExport(exportId));
    }

    @PostMapping("/exports/{exportId}/cancel")
    @Operation(summary = "Cancel an export")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelExport(@PathVariable String exportId) {
        exportCommandService.cancelExport(exportId);
    }

    @DeleteMapping("/exports/{exportId}")
    @Operation(summary = "Delete an export")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExport(@PathVariable String exportId) {
        exportCommandService.deleteExport(exportId);
    }

    // Dataset endpoints

    @GetMapping("/datasets")
    @Operation(summary = "Get all datasets")
    public ResponseEntity<List<com.gogidix.analytics.data.domain.model.AnalyticsDataset>> getDatasets() {
        return ResponseEntity.ok(queryService.getDatasets());
    }

    @GetMapping("/datasets/{datasetId}")
    @Operation(summary = "Get dataset by ID")
    public ResponseEntity<com.gogidix.analytics.data.domain.model.AnalyticsDataset> getDataset(
        @PathVariable String datasetId) {

        return ResponseEntity.ok(queryService.getDataset(datasetId));
    }

    @GetMapping("/datasets/search")
    @Operation(summary = "Search datasets")
    public ResponseEntity<List<com.gogidix.analytics.data.domain.model.AnalyticsDataset>> searchDatasets(
        @RequestParam String search) {

        return ResponseEntity.ok(queryService.searchDatasets(search));
    }
}
