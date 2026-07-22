package com.proyecto.servicio.empresa.service;

import com.proyecto.servicio.empresa.model.request.FiltroComprasAdminRequest;
import com.proyecto.servicio.empresa.model.request.ValidarCompraRequest;
import com.proyecto.servicio.empresa.model.response.BusquedaComprasAdminResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;

public interface AdminComprasService {
    BusquedaComprasAdminResponse listarCompras(FiltroComprasAdminRequest request);
    GenericResponse validarCompra(ValidarCompraRequest request);
}
