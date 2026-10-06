package com.proyecto.servicio.empresa.service.impl;

import com.proyecto.servicio.empresa.entity.sf.Compras;
import com.proyecto.servicio.empresa.entity.sf.DetalleCompra;
import com.proyecto.servicio.empresa.entity.sf.ProductoApp;
import com.proyecto.servicio.empresa.entity.sf.UserProduct;
import com.proyecto.servicio.empresa.entity.sf.Usuario;
import com.proyecto.servicio.empresa.model.request.ExcelExportRequest;
import com.proyecto.servicio.empresa.repositorys.sf.ComprasRepository;
import com.proyecto.servicio.empresa.repositorys.sf.ProductoAppRepository;
import com.proyecto.servicio.empresa.repositorys.sf.UserProductRepository;
import com.proyecto.servicio.empresa.repositorys.sf.UsuarioRepository;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Autowired
    private UserProductRepository userProductRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    @Override
    @Transactional(readOnly = true)
    public byte[] generarExcelProductosAsignados() throws Exception {
        List<UserProduct> asignaciones = userProductRepository.findAllWithProducto();
        log.info("Generando Excel para {} productos asignados", asignaciones.size());

        List<Long> userIds = asignaciones.stream()
            .map(UserProduct::getUserId)
            .distinct()
            .collect(Collectors.toList());

        Map<Long, String> correosMap = usuarioRepository.findByIdIn(userIds)
            .stream()
            .collect(Collectors.toMap(Usuario::getId, Usuario::getCorreo));

        String[] headers = {"Usuario Correo", "Producto", "Cantidad Inicial", "Cantidad Actual"};

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Productos Asignados");
            CellStyle headerStyle = crearEstiloEncabezado(workbook);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 25 * 256);
            }

            int rowNum = 1;
            for (UserProduct up : asignaciones) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(correosMap.getOrDefault(up.getUserId(), ""));
                row.createCell(1).setCellValue(up.getProducto().getNombreProduct());
                row.createCell(2).setCellValue(up.getInitialQuantity() != null ? up.getInitialQuantity() : 0);
                row.createCell(3).setCellValue(up.getCantidad() != null ? up.getCantidad() : 0);
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

    @Override
    @Transactional(readOnly = true)
    public byte[] generarExcelReporteVentas(ExcelExportRequest request) throws Exception {
        List<Compras> compras = obtenerCompras(request);
        log.info("Generando reporte de ventas para {} compras", compras.size());

        // Estadísticas por producto: [vendidas, credito, promocion, merma]
        Map<Long, int[]> statsMap = new LinkedHashMap<>();
        // Montos por producto: [totalEfectivo, totalElectronico, totalCredito]
        Map<Long, double[]> amountsMap = new LinkedHashMap<>();

        for (Compras compra : compras) {

            if (compra.getDetalles() == null || compra.getDetalles().isEmpty()) continue;
            for (DetalleCompra detalle : compra.getDetalles()) {
                Long pid = detalle.getProductoId();
                statsMap.putIfAbsent(pid, new int[4]);
                amountsMap.putIfAbsent(pid, new double[3]);

                int[] s = statsMap.get(pid);
                double[] a = amountsMap.get(pid);
                int cant = detalle.getCantidad() != null ? detalle.getCantidad() : 0;
                double precio = detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario() : 0.0;
                double subtotal = cant * precio;



                int metodoPago = compra.getMetodoPago() != null ? compra.getMetodoPago() : 0;
                boolean promocion = compra.getPromocion() != null && compra.getPromocion() == 1;
                boolean merma = compra.getMerma() != null && compra.getMerma() == 1;
                boolean devolucion = compra.getDevolucion() != null && compra.getDevolucion() == 1;
                if (metodoPago == 0 && !merma && !promocion && !devolucion) {
                    s[0] += cant;
                    a[0] += subtotal;
                }else if (metodoPago == 1 && !merma && !promocion && !devolucion) {
                    s[0] += cant;
                    a[1] += subtotal;
                } else if (metodoPago == 2&& !merma && !promocion && !devolucion) {
                    s[1] += cant;
                    a[2] += subtotal;
                }
                if (promocion) {
                    s[2] += cant;
                }

                if (merma) {
                    s[3] += cant;
                }


            }
        }

        List<Long> productIds = new java.util.ArrayList<>(statsMap.keySet());
        Map<Long, ProductoApp> productosMap = productoAppRepository.findAllById(productIds)
            .stream()
            .collect(Collectors.toMap(ProductoApp::getId, p -> p));

        // Cantidades iniciales/finales desde UserProduct (si hay usuario filtrado)
        Map<Long, UserProduct> userProductMap = new java.util.HashMap<>();
        if (request.getUsuarioCorreo() != null && !request.getUsuarioCorreo().isBlank()) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(request.getUsuarioCorreo());
            if (usuarioOpt.isPresent()) {
                userProductRepository.findByUserId(usuarioOpt.get().getId())
                    .forEach(up -> userProductMap.put(up.getProducto().getId(), up));
            }
        }

        String[] headers = {
            "Producto", "Precio Unitario", "Vendidas", "Credito", "Promocion", "Merma",
            "Total Efectivo", "Total Electrónico", "Total Venta Credito",
            "Cantidad Inicial del Día", "Cantidad Final del Día"
        };

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte de Ventas");
            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle totalesStyle = crearEstiloTotales(workbook);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 22 * 256);
            }

            int rowNum = 1;
            double sumEfectivo = 0, sumElectronico = 0, sumCredito = 0;

            for (Long pid : productIds) {
                ProductoApp producto = productosMap.get(pid);
                int[] s = statsMap.get(pid);
                double[] a = amountsMap.get(pid);
                UserProduct up = userProductMap.get(pid);

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(producto != null ? producto.getNombreProduct() : "");
                row.createCell(1).setCellValue(producto != null && producto.getPrecio() != null ? producto.getPrecio() : 0.0);
                row.createCell(2).setCellValue(s[0]);
                row.createCell(3).setCellValue(s[1]);
                row.createCell(4).setCellValue(s[2]);
                row.createCell(5).setCellValue(s[3]);
                row.createCell(6).setCellValue(a[0]);
                row.createCell(7).setCellValue(a[1]);
                row.createCell(8).setCellValue(a[2]);
                row.createCell(9).setCellValue(up != null && up.getInitialQuantity() != null ? up.getInitialQuantity() : 0);
                row.createCell(10).setCellValue(up != null && up.getCantidad() != null ? up.getCantidad() : 0);

                sumEfectivo += a[0];
                sumElectronico += a[1];
                sumCredito += a[2];
            }

            // Fila TOTALES
            Row totalesRow = sheet.createRow(rowNum++);
            Cell totalesLabel = totalesRow.createCell(0);
            totalesLabel.setCellValue("TOTALES");
            totalesLabel.setCellStyle(totalesStyle);
            for (int i = 1; i <= 5; i++) {
                totalesRow.createCell(i).setCellStyle(totalesStyle);
            }
            Cell cEfectivo = totalesRow.createCell(6);
            cEfectivo.setCellValue(sumEfectivo);
            cEfectivo.setCellStyle(totalesStyle);
            Cell cElectronico = totalesRow.createCell(7);
            cElectronico.setCellValue(sumElectronico);
            cElectronico.setCellStyle(totalesStyle);
            Cell cCredito = totalesRow.createCell(8);
            cCredito.setCellValue(sumCredito);
            cCredito.setCellStyle(totalesStyle);

            // Fila TOTAL GENERAL
            Row totalGeneralRow = sheet.createRow(rowNum);
            Cell tgLabel = totalGeneralRow.createCell(0);
            tgLabel.setCellValue("TOTAL GENERAL");
            tgLabel.setCellStyle(totalesStyle);
            Cell tgValor = totalGeneralRow.createCell(1);
            tgValor.setCellValue(sumEfectivo + sumElectronico + sumCredito);
            tgValor.setCellStyle(totalesStyle);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private CellStyle crearEstiloTotales(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
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
