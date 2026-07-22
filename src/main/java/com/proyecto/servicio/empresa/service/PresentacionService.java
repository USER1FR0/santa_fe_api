package com.proyecto.servicio.empresa.service;

import com.proyecto.servicio.empresa.model.request.ActualizarPresentacionRequest;
import com.proyecto.servicio.empresa.model.request.EliminarPresentacionRequest;
import com.proyecto.servicio.empresa.model.request.PresentacionRequest;
import com.proyecto.servicio.empresa.model.response.BusquedaPresentacionResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.model.response.PresentacionResponse;

public interface PresentacionService {
    PresentacionResponse crearPresentacion(PresentacionRequest request);
    PresentacionResponse actualizarPresentacion(ActualizarPresentacionRequest request);
    GenericResponse eliminarPresentacion(EliminarPresentacionRequest request);
    BusquedaPresentacionResponse listarPresentaciones(boolean soloActivas);
}
