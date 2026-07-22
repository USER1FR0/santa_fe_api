package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarClienteRequest {
    @NotNull
    private Long id;
    private String claveCliente;
    private String nombreCliente;
}
