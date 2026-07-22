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
public class ProductosAsignadosResponse extends GenericResponse {
    private List<ProductoAsignadoItem> productos;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductoAsignadoItem {
        private Long assignedId;
        private Long userId;
        private Long productId;
        private String nombreProduct;
        private String correo;
        private Integer cantidad;
        private Integer initialQuantity;
    }
}
