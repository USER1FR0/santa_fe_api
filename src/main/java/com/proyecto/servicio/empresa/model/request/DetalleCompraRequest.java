package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleCompraRequest {

    @NotNull
    private Long productoId;

    @NotNull
    private Integer cantidad;

    @NotNull
    private Double precioUnitario;
}
