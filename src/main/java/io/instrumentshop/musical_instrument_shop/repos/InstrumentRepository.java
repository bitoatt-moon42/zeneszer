package io.instrumentshop.musical_instrument_shop.repos;

import io.instrumentshop.musical_instrument_shop.domain.Instrument;

import java.util.List;
import java.util.UUID;

import io.instrumentshop.musical_instrument_shop.model.InstrumentType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InstrumentRepository extends JpaRepository<Instrument, UUID> {

    List<Instrument> findByType(InstrumentType type, Sort sort);
}
