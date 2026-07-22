package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.aspectj.bridge.IMessage;

@Getter
@Setter
@NoArgsConstructor
@Data
public class BuscarUserRequest {

    private String nombre;

    private Long pagina;

    @NotNull(message = "El tamaño de página no puede ser nulo")
    @Min(value = 1, message = "El tamaño de página debe ser mayor o igual a 1")
    private Integer tamanoPagina;


}
