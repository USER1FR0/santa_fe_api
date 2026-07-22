package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroComprasAdminRequest {
    private String usuarioCorreo;
    private String fechaInicio;
    private String fechaFin;
    @NotNull
    private Long pagina;
    @NotNull
    private Long tamanoPagina;
}
