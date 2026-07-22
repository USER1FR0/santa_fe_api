package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListarProductosAsignadosRequest {
    @NotBlank
    private String correo;
}
