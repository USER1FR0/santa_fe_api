package com.proyecto.servicio.empresa.controller;

import com.proyecto.servicio.empresa.model.request.ActualizarClienteRequest;
import com.proyecto.servicio.empresa.model.request.ClienteRequest;
import com.proyecto.servicio.empresa.model.request.EliminarClienteRequest;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.service.ClienteService;
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

@Slf4j
@RestController
@RequestMapping("/API/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ValidadorCampos validacionResponse;

    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente")
    @PostMapping(value = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> crearCliente(@Valid @RequestBody ClienteRequest request, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(clienteService.crearCliente(request), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza datos de un cliente")
    @PostMapping(value = "/actualizar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> actualizarCliente(@Valid @RequestBody ActualizarClienteRequest request, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(clienteService.actualizarCliente(request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por id")
    @PostMapping(value = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> eliminarCliente(@Valid @RequestBody EliminarClienteRequest request, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(clienteService.eliminarCliente(request), HttpStatus.OK);
    }

    @Operation(summary = "Listar clientes", description = "Obtiene todos los clientes")
    @PostMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> listarClientes() {
        return new ResponseEntity<>(clienteService.listarClientes(), HttpStatus.OK);
    }
}
