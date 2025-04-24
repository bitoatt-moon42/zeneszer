package io.instrumentshop.musical_instrument_shop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InstrumentDTO {

    private UUID id;

    @NotNull
    private InstrumentType type;

    @NotNull
    @Size(max = 30)
    private String brand;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "75.08")
    private BigDecimal price;

    @Size(max = 2048)
    private String imageUrl;

}
