package com.proyecto.servicio.empresa.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusquedaClienteResponse extends GenericResponse {
    private List<ClienteResponse> clientes;
    private Integer totalPaginas;
    private Long totalElementos;
}
