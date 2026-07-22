package com.proyecto.servicio.empresa.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductoResponse extends GenericResponse {
    private Long idProducto;
    private String claveProduct;
    private String nombreProduct;
    private Double precio;
    private Integer cantidadProducto;
    private String imagen;
    private String lugar;
    private String claveSucursal;
    private String claveMoneda;
    private String claveMonD;
    private String unidad;
    private String presentacionId;
}
