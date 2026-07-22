package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SincronizarComprasRequest {

    @NotBlank
    private String usuarioCorreo;

    private String claveAgente;

    @NotBlank
    private String captureId;

    @NotNull
    private Double total;

    private String claveCliente;

    @NotBlank
    private String fecha;

    private String direccionEnvio;

    private Double latitud;

    private Double longitud;

    private Integer metodoPago = 0;

    private Integer devolucion = 0;

    private Integer promocion = 0;

    private Integer merma = 0;

    private Integer consecutivo = 0;

    @NotNull
    @Valid
    private List<DetalleCompraRequest> detalles;
}
