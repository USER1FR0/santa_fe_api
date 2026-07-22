package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductoRequest {
    private String claveProduct;
    @NotEmpty(message = "El nombre no puede estar vacía")
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombreProduct;
    @NotNull(message = "El precio no puede ser nulo")
    private Double precio;
    @NotNull(message = "La cantidadProducto no puede ser nulo")
    private Integer cantidadProducto;
    private String imagen;
    private String lugar;
    private String claveSucursal;
    private String claveMoneda;
    private String claveMonD;
    private String unidad;
    private Long presentacionId;
}