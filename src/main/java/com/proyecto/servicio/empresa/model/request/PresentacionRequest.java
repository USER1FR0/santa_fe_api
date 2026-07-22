package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentacionRequest {
    @NotNull
    private Integer gramos;
    private String etiqueta;
    private Boolean activo = true;
}
