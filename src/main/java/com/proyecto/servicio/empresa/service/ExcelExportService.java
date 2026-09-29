package com.proyecto.servicio.empresa.service;

import com.proyecto.servicio.empresa.model.request.ExcelExportRequest;

public interface ExcelExportService {
    byte[] generarExcelPedidos(ExcelExportRequest request) throws Exception;
    byte[] generarExcelProductosAsignados() throws Exception;
    byte[] generarExcelReporteVentas(ExcelExportRequest request) throws Exception;
}
