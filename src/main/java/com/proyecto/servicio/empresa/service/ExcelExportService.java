package com.proyecto.servicio.empresa.service;

import com.proyecto.servicio.empresa.model.request.ExcelExportRequest;

public interface ExcelExportService {
    byte[] generarExcelPedidos(ExcelExportRequest request) throws Exception;
}
