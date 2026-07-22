package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarCompraRequest {
    @NotNull
    private Long idCompra;
}
