package com.proyecto.servicio.empresa.controller;

import com.proyecto.servicio.empresa.model.request.FiltroComprasAdminRequest;
import com.proyecto.servicio.empresa.model.request.ValidarCompraRequest;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.service.AdminComprasService;
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
@RequestMapping("/API/v1/admin/compras")
public class AdminComprasController {

    @Autowired
    private AdminComprasService adminComprasService;

    @Autowired
    private ValidadorCampos validacionResponse;

    @Operation(summary = "Listar compras (admin)", description = "Consulta compras con filtros opcionales por correo de usuario y rango de fechas")
    @PostMapping(value = "/listar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> listarCompras(@Valid @RequestBody FiltroComprasAdminRequest request, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(adminComprasService.listarCompras(request), HttpStatus.OK);
    }

    @Operation(summary = "Validar compra", description = "Marca una compra como validada")
    @PostMapping(value = "/validar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> validarCompra(@Valid @RequestBody ValidarCompraRequest request, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(adminComprasService.validarCompra(request), HttpStatus.OK);
    }
}
