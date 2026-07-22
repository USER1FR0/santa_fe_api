package com.proyecto.servicio.empresa.controller;

import com.proyecto.servicio.empresa.model.request.SincronizarComprasRequest;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.service.ComprasService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/API/v1/compras")
public class ComprasController {

    @Autowired
    private ComprasService comprasService;

    @Autowired
    private ValidadorCampos validacionResponse;

    @Operation(
        summary = "Sincronizar ventas del día",
        description = "Recibe una lista de compras desde la app Flutter y las guarda en PostgreSQL. Ignora duplicados por capture_id."
    )
    @PostMapping(
        value = "/sincronizar",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GenericResponse> sincronizar(
        @Valid @RequestBody List<SincronizarComprasRequest> compras,
        Errors errors
    ) {
        if (errors != null && errors.hasErrors()) {
            log.warn("Errores en la solicitud de sincronización: {}", errors.getAllErrors());
            return validacionResponse.dtoValidation(errors);
        }
        return new ResponseEntity<>(comprasService.sincronizarCompras(compras), HttpStatus.OK);
    }
}
