package io.instrumentshop.musical_instrument_shop.util;


import io.instrumentshop.musical_instrument_shop.domain.Instrument;
import io.instrumentshop.musical_instrument_shop.domain.User;
import io.instrumentshop.musical_instrument_shop.model.InstrumentType;
import io.instrumentshop.musical_instrument_shop.repos.InstrumentRepository;
import io.instrumentshop.musical_instrument_shop.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        createInstruments("Gibson", "Les Paul", InstrumentType.GUITAR, 2000, "https://fast-images.static-thomann.de/pics/bdb/_60/601263/19748306_800.jpg");
        createInstruments("Fender", "Stratocaster", InstrumentType.GUITAR, 1000, "https://img.kytary.com/eshop_hu/stredni_pdp/na/638726341026266667/23ab749a/65326578/fender-player-ii-modified-stratocaster-mn-3ts.avif");
        createInstruments("Ibanez", "GIO", InstrumentType.GUITAR, 150, "https://fast-images.static-thomann.de/pics/bdb/_35/356054/16017988_800.jpg");
        createInstruments("ESP", "LTD EX-80", InstrumentType.GUITAR, 300, "https://fast-images.static-thomann.de/pics/bdb/_43/431690/13463741_800.jpg");
        createInstruments("Gibson", "Thunderbird", InstrumentType.BASS_GUITAR, 1700, "https://fast-images.static-thomann.de/pics/bdb/_15/151552/17882937_800.jpg");
        createInstruments("Fender", "Jazz Bass", InstrumentType.BASS_GUITAR, 1000, "https://fast-images.static-thomann.de/pics/bdb/_50/500276/18778533_800.jpg");
        createInstruments("Fender", "Precision Bass", InstrumentType.BASS_GUITAR, 12000, "https://img.kytary.com/eshop_hu/stredni_pdp/na/638797122616200000/e58739a4/65366455/fender-player-ii-modified-active-precision-bass-mn-3ts.avif");
        createInstruments("Tama", "Imperialstar", InstrumentType.DRUM, 2000, "https://img.kytary.com/eshop_hu/velky_v2/na/638082567842670000/3193788a/65051528/tama-imperialstar-blacked-out-black-rock-set.avif");
        createInstruments("SONOR", "AQX Stage", InstrumentType.DRUM, 2000, "https://img.kytary.com/eshop_hu/stredni_pdp/na/637599435726030000/aa5269b0/64871460/sonor-aqx-studio-set-black-midnight-sparkle.avif");
        createInstruments("Millenium", "Focus", InstrumentType.DRUM, 155, "https://thumbs.static-thomann.de/thumb/padthumb600x600/pics/bdb/_49/495981/15775405_800.jpg");
        User user = new User();
        user.setPassword("admin");
        user.setName("admin");
        userRepository.save(user);
    }

    private void createInstruments(String brand, String name, InstrumentType type, Integer price, String imageUrl) {
        Instrument i = new Instrument();
        i.setBrand(brand);
        i.setName(name);
        i.setType(type);
        i.setPrice(BigDecimal.valueOf(price));
        i.setImageUrl(imageUrl);
        instrumentRepository.save(i);
    }
}
