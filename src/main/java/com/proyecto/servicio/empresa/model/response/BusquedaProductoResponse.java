package com.proyecto.servicio.empresa.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.proyecto.servicio.empresa.entity.sf.ProductoApp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusquedaProductoResponse extends GenericResponse {
    private List<ProductoResponse> productoApps;
    private int totalPaginas;
    private long totalElementos;
}
