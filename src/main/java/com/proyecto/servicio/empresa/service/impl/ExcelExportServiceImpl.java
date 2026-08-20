package com.proyecto.servicio.empresa.service.impl;

import com.proyecto.servicio.empresa.entity.sf.Compras;
import com.proyecto.servicio.empresa.entity.sf.DetalleCompra;
import com.proyecto.servicio.empresa.entity.sf.ProductoApp;
import com.proyecto.servicio.empresa.model.request.ExcelExportRequest;
import com.proyecto.servicio.empresa.repositorys.sf.ComprasRepository;
import com.proyecto.servicio.empresa.repositorys.sf.ProductoAppRepository;
import com.proyecto.servicio.empresa.service.ExcelExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    private static final String[] HEADERS = {
        "NO_PED", "CVE_SUC", "LUGAR", "F_ALTA_PED", "CVE_CTE", "CVE_AGE",
        "CVE_MON", "P_Y_D", "CVE_MONM", "TIP_CAM", "PED_INT", "FECHA_ENT",
        "DESCUE", "DESCUE2", "DESCUE3", "DESCUE4", "CVE_PROD", "CODEBAR",
        "NEW_MED", "CANT_PROD", "CVE_MON_D", "VALOR_PROD", "UNIDAD",
        "DCTO1", "DCTO2", "DETA_PROD", "OBSERVA"
    };

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private ProductoAppRepository productoAppRepository;

    @Override
    @Transactional(readOnly = true)
    public byte[] generarExcelPedidos(ExcelExportRequest request) throws Exception {
        List<Compras> compras = obtenerCompras(request);
        log.info("Generando Excel para {} pedidos", compras.size());

        List<Long> productIds = compras.stream()
            .filter(c -> c.getDetalles() != null)
            .flatMap(c -> c.getDetalles().stream())
            .map(DetalleCompra::getProductoId)
            .distinct()
            .collect(Collectors.toList());

        Map<Long, ProductoApp> productosMap = productoAppRepository.findAllById(productIds)
            .stream()
            .collect(Collectors.toMap(ProductoApp::getId, p -> p));

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Pedidos");
            CellStyle headerStyle = crearEstiloEncabezado(workbook);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 18 * 256);
            }

            int rowNum = 1;
            for (Compras compra : compras) {
                if (compra.getDetalles() == null || compra.getDetalles().isEmpty()) continue;
                for (DetalleCompra detalle : compra.getDetalles()) {
                    ProductoApp producto = productosMap.get(detalle.getProductoId());
                    llenarFila(sheet.createRow(rowNum++), compra, detalle, producto);
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private List<Compras> obtenerCompras(ExcelExportRequest request) {
        boolean tieneCorreo = request.getUsuarioCorreo() != null && !request.getUsuarioCorreo().isBlank();
        boolean tieneFechas = request.getFechaInicio() != null && request.getFechaFin() != null;

        if (tieneCorreo && tieneFechas) {
            return comprasRepository.findByUsuarioCorreoAndFechaBetween(
                request.getUsuarioCorreo(), request.getFechaInicio(), request.getFechaFin());
        }
        if (tieneFechas) {
            return comprasRepository.findByFechaBetween(request.getFechaInicio(), request.getFechaFin());
        }
        if (tieneCorreo) {
            return comprasRepository.findByUsuarioCorreo(request.getUsuarioCorreo());
        }
        return comprasRepository.findAll();
    }

    private void llenarFila(Row row, Compras compra, DetalleCompra detalle, ProductoApp producto) {
        setValor(row, 0, compra.getConsecutivo());
        setValor(row, 1, producto != null ? producto.getClaveSucursal() : "");
        setValor(row, 2, producto != null ? producto.getLugar() : "");
        setValor(row, 3, formatearFecha(compra.getFecha()));
        setValor(row, 4, compra.getClaveCliente());
        setValor(row, 5, compra.getClaveAgente());
        setValor(row, 6, producto != null ? producto.getClaveMoneda() : "");
        setValor(row, 7, "");   // P_Y_D  - no disponible en entidades
        setValor(row, 8, "");   // CVE_MONM
        setValor(row, 9, "");   // TIP_CAM
        setValor(row, 10, compra.getCaptureId());
        setValor(row, 11, "");  // FECHA_ENT
        setValor(row, 12, "");  // DESCUE
        setValor(row, 13, "");  // DESCUE2
        setValor(row, 14, "");  // DESCUE3
        setValor(row, 15, "");  // DESCUE4
        setValor(row, 16, producto != null ? producto.getClaveProduct() : "");
        setValor(row, 17, "");  // CODEBAR
        setValor(row, 18, "");  // NEW_MED
        setValor(row, 19, detalle.getCantidad());
        setValor(row, 20, producto != null ? producto.getClaveMonD() : "");
        setValor(row, 21, detalle.getPrecioUnitario());
        setValor(row, 22, producto != null ? producto.getUnidad() : "");
        setValor(row, 23, "");  // DCTO1
        setValor(row, 24, "");  // DCTO2
        setValor(row, 25, "");  // DETA_PROD
        setValor(row, 26, "");  // OBSERVA
    }

    private String formatearFecha(String fecha) {
        if (fecha == null) return "";
        try {
            LocalDate date = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            return fecha;
        }
    }

    private void setValor(Row row, int col, Object valor) {
        Cell cell = row.createCell(col);
        if (valor == null) {
            cell.setCellValue("");
        } else if (valor instanceof Integer i) {
            cell.setCellValue(i);
        } else if (valor instanceof Double d) {
            cell.setCellValue(d);
        } else {
            cell.setCellValue(valor.toString());
        }
    }

    private CellStyle crearEstiloEncabezado(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }
}
