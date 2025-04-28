package io.instrumentshop.musical_instrument_shop.service;

import io.instrumentshop.musical_instrument_shop.domain.Instrument;
import io.instrumentshop.musical_instrument_shop.model.InstrumentDTO;
import io.instrumentshop.musical_instrument_shop.model.InstrumentType;
import io.instrumentshop.musical_instrument_shop.repos.InstrumentRepository;
import io.instrumentshop.musical_instrument_shop.util.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class InstrumentServiceTest {

    private InstrumentRepository instrumentRepository;
    private InstrumentService instrumentService;

    @BeforeEach
    void setUp() {
        instrumentRepository = mock(InstrumentRepository.class);
        instrumentService = new InstrumentService(instrumentRepository);
    }

    @Test
    void testFindByType_withValidType() {
        Instrument instrument = createSampleInstrument();
        when(instrumentRepository.findByType(eq(InstrumentType.GUITAR), any()))
            .thenReturn(List.of(instrument));

        List<InstrumentDTO> result = instrumentService.findByType("GUITAR");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Stratocaster");
    }

    @Test
    void testGet_existingInstrument() {
        Instrument instrument = createSampleInstrument();
        when(instrumentRepository.findById(any()))
            .thenReturn(Optional.of(instrument));

        InstrumentDTO dto = instrumentService.get(UUID.randomUUID());

        assertThat(dto.getName()).isEqualTo("Stratocaster");
    }

    @Test
    void testGet_nonExistingInstrument() {
        when(instrumentRepository.findById(any()))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> instrumentService.get(UUID.randomUUID()));
    }

    @Test
    void testCreate_savesInstrument() {
        Instrument instrument = createSampleInstrument();
        when(instrumentRepository.save(any()))
            .thenReturn(instrument);

        InstrumentDTO dto = createSampleInstrumentDTO();
        UUID id = instrumentService.create(dto);

        assertThat(id).isNotNull();
        verify(instrumentRepository, times(1)).save(any());
    }

    @Test
    void testUpdate_existingInstrument() {
        Instrument instrument = createSampleInstrument();
        when(instrumentRepository.findById(any()))
            .thenReturn(Optional.of(instrument));
        when(instrumentRepository.save(any()))
            .thenReturn(instrument);

        InstrumentDTO dto = createSampleInstrumentDTO();
        instrumentService.update(UUID.randomUUID(), dto);

        verify(instrumentRepository, times(1)).save(any());
    }

    @Test
    void testUpdate_nonExistingInstrument() {
        when(instrumentRepository.findById(any()))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            instrumentService.update(UUID.randomUUID(), createSampleInstrumentDTO())
        );
    }

    @Test
    void testDelete_callsRepositoryDelete() {
        UUID id = UUID.randomUUID();

        instrumentService.delete(id);

        verify(instrumentRepository, times(1)).deleteById(id);
    }

    private Instrument createSampleInstrument() {
        Instrument instrument = new Instrument();
        instrument.setId(UUID.randomUUID());
        instrument.setType(InstrumentType.GUITAR);
        instrument.setBrand("Fender");
        instrument.setName("Stratocaster");
        instrument.setPrice(BigDecimal.valueOf(1200));
        instrument.setImageUrl("http://example.com/strat.jpg");
        return instrument;
    }

    private InstrumentDTO createSampleInstrumentDTO() {
        InstrumentDTO dto = new InstrumentDTO();
        dto.setType(InstrumentType.GUITAR);
        dto.setBrand("Fender");
        dto.setName("Stratocaster");
        dto.setPrice(BigDecimal.valueOf(1200));
        dto.setImageUrl("http://example.com/strat.jpg");
        return dto;
    }
}
