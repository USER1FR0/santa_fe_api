package com.proyecto.servicio.empresa.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelExportRequest {
    private String usuarioCorreo;
    private String fechaInicio;
    private String fechaFin;
}
