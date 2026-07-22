package com.proyecto.servicio.empresa.controller;

import com.proyecto.servicio.empresa.model.request.ActualizarProductoRequest;
import com.proyecto.servicio.empresa.model.request.BuscarProductoRequest;
import com.proyecto.servicio.empresa.model.request.DesasignarProductoRequest;
import com.proyecto.servicio.empresa.model.request.EliminarProductoRequest;
import com.proyecto.servicio.empresa.model.request.ListarProductosAsignadosRequest;
import com.proyecto.servicio.empresa.model.request.ProductoAsignadoRequest;
import com.proyecto.servicio.empresa.model.request.ProductoRequest;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.service.ProductoService;
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
@RequestMapping("/API/v1/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ValidadorCampos validacionResponse;

    @Operation(summary = "Agrega productos", description = "agrega al catalogo de productos")
    @PostMapping(value = "/agregar",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> agregarProductos(@Valid @RequestBody ProductoRequest productoRequest, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(productoService.agregarProducto(productoRequest), HttpStatus.OK);
    }
    @Operation(summary = "listar productos", description = "listar el catalogo de productos")
    @PostMapping(value = "/listarProductos",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> listarProductos(@Valid @RequestBody BuscarProductoRequest productoRequest, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(productoService.buscarProductoPorId(productoRequest), HttpStatus.OK);
    }
    @Operation(summary = "agregar productos", description = "lista de productos asignados")
    @PostMapping(value = "/asignarProductos",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> asignarProductos(@Valid @RequestBody ProductoAsignadoRequest productoRequest, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(productoService.asignarProducto(productoRequest), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza datos de un producto existente")
    @PostMapping(value = "/actualizar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> actualizarProducto(@Valid @RequestBody ActualizarProductoRequest productoRequest, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(productoService.actualizarProducto(productoRequest), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo")
    @PostMapping(value = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> eliminarProducto(@Valid @RequestBody EliminarProductoRequest productoRequest, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(productoService.eliminarProducto(productoRequest), HttpStatus.OK);
    }

    @Operation(summary = "Desasignar producto", description = "Elimina una asignación y devuelve el stock al almacén")
    @PostMapping(value = "/desasignarProducto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> desasignarProducto(@Valid @RequestBody DesasignarProductoRequest request, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(productoService.desasignarProducto(request), HttpStatus.OK);
    }

    @Operation(summary = "Listar productos asignados", description = "Devuelve todos los productos asignados a un usuario por su correo")
    @PostMapping(value = "/listarAsignados", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> listarAsignados(@Valid @RequestBody ListarProductosAsignadosRequest request, Errors errors) throws ParseException {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(productoService.listarProductosAsignados(request), HttpStatus.OK);
    }
}
