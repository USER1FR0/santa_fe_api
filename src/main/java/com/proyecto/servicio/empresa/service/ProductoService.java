package com.proyecto.servicio.empresa.service;

import com.proyecto.servicio.empresa.model.request.ActualizarProductoRequest;
import com.proyecto.servicio.empresa.model.request.BuscarProductoRequest;
import com.proyecto.servicio.empresa.model.request.DesasignarProductoRequest;
import com.proyecto.servicio.empresa.model.request.EliminarProductoRequest;
import com.proyecto.servicio.empresa.model.request.ListarProductosAsignadosRequest;
import com.proyecto.servicio.empresa.model.request.ProductoAsignadoRequest;
import com.proyecto.servicio.empresa.model.request.ProductoRequest;
import com.proyecto.servicio.empresa.model.response.BusquedaProductoResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.model.response.ProductoAsignadoResponse;
import com.proyecto.servicio.empresa.model.response.ProductoResponse;
import com.proyecto.servicio.empresa.model.response.ProductosAsignadosResponse;

public interface ProductoService {
    ProductoResponse agregarProducto(ProductoRequest productoRequest);
    ProductoResponse actualizarProducto(ActualizarProductoRequest request);
    BusquedaProductoResponse buscarProductoPorId(BuscarProductoRequest request);
    ProductoAsignadoResponse asignarProducto(ProductoAsignadoRequest request);
    GenericResponse desasignarProducto(DesasignarProductoRequest request);
    ProductosAsignadosResponse listarProductosAsignados(ListarProductosAsignadosRequest request);
    GenericResponse eliminarProducto(EliminarProductoRequest request);
}
