package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesasignarProductoRequest {
    @NotNull
    private Long userProductId;
    @NotNull
    private Long productoId;
    @NotNull
    private Integer cantidad;
}
