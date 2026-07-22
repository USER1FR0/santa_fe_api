package com.proyecto.servicio.empresa.service;

import com.proyecto.servicio.empresa.model.request.ActualizarClienteRequest;
import com.proyecto.servicio.empresa.model.request.ClienteRequest;
import com.proyecto.servicio.empresa.model.request.EliminarClienteRequest;
import com.proyecto.servicio.empresa.model.response.BusquedaClienteResponse;
import com.proyecto.servicio.empresa.model.response.ClienteResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;

public interface ClienteService {
    ClienteResponse crearCliente(ClienteRequest request);
    ClienteResponse actualizarCliente(ActualizarClienteRequest request);
    GenericResponse eliminarCliente(EliminarClienteRequest request);
    BusquedaClienteResponse listarClientes();
}
