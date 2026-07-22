package com.proyecto.servicio.empresa.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductoAsignadoRequest {
    private String correo;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
}
