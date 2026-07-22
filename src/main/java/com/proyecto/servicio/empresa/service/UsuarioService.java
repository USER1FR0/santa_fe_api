package com.proyecto.servicio.empresa.service;

import com.proyecto.servicio.empresa.model.request.*;
import com.proyecto.servicio.empresa.model.response.BusquedaUserResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.model.response.LoginResponse;
import com.proyecto.servicio.empresa.model.response.UsuarioResponse;


public interface UsuarioService {
    GenericResponse eliminarUser(EliminarUserRequest eliminarUserRequest);

    UsuarioResponse registrarUsuario(UsuarioRequest request);
    UsuarioResponse actualizarUsuario(ActualizaUserRequest request);
    BusquedaUserResponse buscarUsuario(BuscarUserRequest request);
    LoginResponse login(LoginRequest request);
}
