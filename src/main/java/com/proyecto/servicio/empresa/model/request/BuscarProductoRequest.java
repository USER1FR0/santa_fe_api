package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class BuscarProductoRequest {
    private String nombreProducto;

    private Long pagina;

    @NotNull(message = "El tamaño de página no puede ser nulo")
    @Min(value = 1, message = "El tamaño de página debe ser mayor o igual a 1")
    private Integer tamanoPagina;
}
