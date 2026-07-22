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
public class CompraAdminResponse {
    private Long id;
    private String usuarioCorreo;
    private String claveAgente;
    private String captureId;
    private Double total;
    private String claveCliente;
    private String fecha;
    private String direccionEnvio;
    private Integer validado;
    private String metodoPago;
    private Integer devolucion;
    private Integer promocion;
    private Integer merma;
    private Integer consecutivo;
    private List<DetalleCompraResponse> detalles;
}
