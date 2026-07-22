package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActualizarProductoRequest {
    @NotNull(message = "El idProducto no puede ser nulo")
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
