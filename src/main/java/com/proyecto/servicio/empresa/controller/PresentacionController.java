package com.proyecto.servicio.empresa.controller;

import com.proyecto.servicio.empresa.model.request.ActualizarPresentacionRequest;
import com.proyecto.servicio.empresa.model.request.EliminarPresentacionRequest;
import com.proyecto.servicio.empresa.model.request.PresentacionRequest;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.service.PresentacionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/API/v1/presentaciones")
public class PresentacionController {

    @Autowired
    private PresentacionService presentacionService;

    @Autowired
    private ValidadorCampos validacionResponse;

    @Operation(summary = "Crear presentación", description = "Registra una nueva presentación/gramaje")
    @PostMapping(value = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> crearPresentacion(@Valid @RequestBody PresentacionRequest request, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(presentacionService.crearPresentacion(request), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar presentación", description = "Actualiza una presentación existente")
    @PostMapping(value = "/actualizar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> actualizarPresentacion(@Valid @RequestBody ActualizarPresentacionRequest request, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(presentacionService.actualizarPresentacion(request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar presentación", description = "Elimina una presentación por id")
    @PostMapping(value = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> eliminarPresentacion(@Valid @RequestBody EliminarPresentacionRequest request, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(presentacionService.eliminarPresentacion(request), HttpStatus.OK);
    }

    @Operation(summary = "Listar presentaciones", description = "Obtiene el catálogo de presentaciones. Param soloActivas=true para filtrar inactivas.")
    @GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> listarPresentaciones(
            @RequestParam(defaultValue = "false") boolean soloActivas) {
        return new ResponseEntity<>(presentacionService.listarPresentaciones(soloActivas), HttpStatus.OK);
    }
}
