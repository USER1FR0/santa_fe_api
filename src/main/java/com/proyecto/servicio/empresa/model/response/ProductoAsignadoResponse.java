package com.proyecto.servicio.empresa.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductoAsignadoResponse extends GenericResponse {
    private String correo;
    private String nombreProducto;
    private Integer cantidad;
}
