package io.instrumentshop.musical_instrument_shop.service;

import io.instrumentshop.musical_instrument_shop.domain.Instrument;
import io.instrumentshop.musical_instrument_shop.model.InstrumentDTO;
import io.instrumentshop.musical_instrument_shop.model.InstrumentType;
import io.instrumentshop.musical_instrument_shop.repos.InstrumentRepository;
import io.instrumentshop.musical_instrument_shop.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;

    public InstrumentService(final InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    public List<InstrumentDTO> findByType(String type) {
        final List<Instrument> instruments;
        if (type != null) {
            instruments = instrumentRepository.findByType(InstrumentType.valueOf(type.toUpperCase()), Sort.by("brand", "name"));
        } else {
            instruments = instrumentRepository.findAll(Sort.by("brand", "name"));
        }
        return instruments.stream()
            .map(instrument -> mapToDTO(instrument, new InstrumentDTO()))
            .toList();
    }

    public InstrumentDTO get(final UUID id) {
        return instrumentRepository.findById(id)
            .map(instrument -> mapToDTO(instrument, new InstrumentDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public UUID create(final InstrumentDTO instrumentDTO) {
        final Instrument instrument = new Instrument();
        mapToEntity(instrumentDTO, instrument);
        return instrumentRepository.save(instrument).getId();
    }

    public void update(final UUID id, final InstrumentDTO instrumentDTO) {
        final Instrument instrument = instrumentRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(instrumentDTO, instrument);
        instrumentRepository.save(instrument);
    }

    public void delete(final UUID id) {
        instrumentRepository.deleteById(id);
    }

    private InstrumentDTO mapToDTO(final Instrument instrument, final InstrumentDTO instrumentDTO) {
        instrumentDTO.setId(instrument.getId());
        instrumentDTO.setType(instrument.getType());
        instrumentDTO.setBrand(instrument.getBrand());
        instrumentDTO.setName(instrument.getName());
        instrumentDTO.setPrice(instrument.getPrice());
        instrumentDTO.setImageUrl(instrument.getImageUrl());
        return instrumentDTO;
    }

    private Instrument mapToEntity(final InstrumentDTO instrumentDTO, final Instrument instrument) {
        instrument.setType(instrumentDTO.getType());
        instrument.setBrand(instrumentDTO.getBrand());
        instrument.setName(instrumentDTO.getName());
        instrument.setPrice(instrumentDTO.getPrice());
        instrument.setImageUrl(instrumentDTO.getImageUrl());
        return instrument;
    }
}
