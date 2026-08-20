package com.proyecto.servicio.empresa.controller;

import com.proyecto.servicio.empresa.model.request.ExcelExportRequest;
import com.proyecto.servicio.empresa.service.ExcelExportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/API/v1/excel")
public class ExcelController {

    @Autowired
    private ExcelExportService excelExportService;

    @Operation(
        summary = "Exportar pedidos a Excel (formato SAI)",
        description = "Genera un .xlsx con los pedidos en el formato de importación de SAI. "
            + "Filtros opcionales: usuarioCorreo, fechaInicio, fechaFin. "
            + "Sin filtros devuelve todos los pedidos."
    )
    @PostMapping(
        value = "/exportar-pedidos",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<byte[]> exportarPedidos(@RequestBody ExcelExportRequest request) {
        try {
            byte[] excel = excelExportService.generarExcelPedidos(request);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"pedidos_sai.xlsx\"")
                .contentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
        } catch (Exception e) {
            log.error("Error al generar Excel de pedidos: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
