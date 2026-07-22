package com.proyecto.servicio.empresa.controller;

import com.proyecto.servicio.empresa.model.request.*;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.repositorys.sf.UsuarioRepository;
import com.proyecto.servicio.empresa.service.UsuarioService;
import com.proyecto.servicio.empresa.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/API/v1/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ValidadorCampos validacionResponse;


    @Operation(summary = "Eliminar usuarios", description = "Elimina  usuarios")
    @PostMapping(value = "/eliminar",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> eliminarUser(@Valid @RequestBody EliminarUserRequest usuario, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(usuarioService.eliminarUser(usuario), HttpStatus.OK);
    }
    @Operation(summary = "login usuarios", description = "valida usuarios")
    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> loginUser(@Valid @RequestBody LoginRequest usuario, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }



        return new ResponseEntity<>(usuarioService.login(usuario), HttpStatus.OK);
    }

    @Operation(summary = "Crear nuevo usuario", description = "Registra nuevos usuarios")
    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> create(@Valid @RequestBody UsuarioRequest usuario, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(usuarioService.registrarUsuario(usuario), HttpStatus.OK);
    }
    @Operation(summary = "Actualiza nuevo usuario", description = "Actualiza nuevos usuarios")
    @PostMapping(value = "/actualizar",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> actualiza(@Valid @RequestBody ActualizaUserRequest usuario, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(usuarioService.actualizarUsuario(usuario), HttpStatus.OK);
    }

    @Operation(summary = "buscar usuario", description = "busqueda de  nuevos usuarios")
    @PostMapping(value = "/buscarUsuario",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> buscarUser(@Valid @RequestBody BuscarUserRequest usuario, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(usuarioService.buscarUsuario(usuario), HttpStatus.OK);
    }
}



