package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarPresentacionRequest {
    @NotNull
    private Long id;
    private Integer gramos;
    private String etiqueta;
    private Boolean activo;
}
