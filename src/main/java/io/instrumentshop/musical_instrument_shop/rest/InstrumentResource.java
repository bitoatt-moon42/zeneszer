package io.instrumentshop.musical_instrument_shop.rest;

import io.instrumentshop.musical_instrument_shop.model.InstrumentDTO;
import io.instrumentshop.musical_instrument_shop.service.InstrumentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/instruments", produces = MediaType.APPLICATION_JSON_VALUE)
public class InstrumentResource {

    private final InstrumentService instrumentService;

    public InstrumentResource(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping
    public ResponseEntity<List<InstrumentDTO>> getAllInstruments(String type) {
        return ResponseEntity.ok(instrumentService.findByType(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstrumentDTO> getInstrument(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(instrumentService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createInstrument(
            @RequestBody @Valid final InstrumentDTO instrumentDTO) {
        final UUID createdId = instrumentService.create(instrumentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateInstrument(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final InstrumentDTO instrumentDTO) {
        instrumentService.update(id, instrumentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteInstrument(@PathVariable(name = "id") final UUID id) {
        instrumentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
