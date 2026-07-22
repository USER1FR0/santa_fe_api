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
public class BusquedaComprasAdminResponse extends GenericResponse {
    private List<CompraAdminResponse> compras;
    private Integer totalPaginas;
    private Long totalElementos;
}
