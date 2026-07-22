package com.proyecto.servicio.empresa.service.impl;

import com.proyecto.servicio.empresa.entity.sf.Compras;
import com.proyecto.servicio.empresa.model.request.FiltroComprasAdminRequest;
import com.proyecto.servicio.empresa.model.request.ValidarCompraRequest;
import com.proyecto.servicio.empresa.model.response.BusquedaComprasAdminResponse;
import com.proyecto.servicio.empresa.model.response.CompraAdminResponse;
import com.proyecto.servicio.empresa.model.response.DetalleCompraResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.repositorys.sf.ComprasRepository;
import com.proyecto.servicio.empresa.service.AdminComprasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminComprasServiceImpl implements AdminComprasService {

    @Autowired
    private ComprasRepository comprasRepository;

    @Override
    public BusquedaComprasAdminResponse listarCompras(FiltroComprasAdminRequest request) {
        BusquedaComprasAdminResponse response = new BusquedaComprasAdminResponse();
        try {
            Pageable pageable = PageRequest.of(
                    Math.toIntExact(request.getPagina()),
                    Math.toIntExact(request.getTamanoPagina()),
                    Sort.by(Sort.Direction.DESC, "id")
            );

            Page<Compras> pagina;
            boolean tieneCorreo = request.getUsuarioCorreo() != null && !request.getUsuarioCorreo().isBlank();
            boolean tieneFechas = request.getFechaInicio() != null && request.getFechaFin() != null;

            if (tieneCorreo && tieneFechas) {
                pagina = comprasRepository.findByUsuarioCorreoAndFechaBetween(
                        request.getUsuarioCorreo(), request.getFechaInicio(), request.getFechaFin(), pageable);
            } else if (tieneCorreo) {
                pagina = comprasRepository.findByUsuarioCorreo(request.getUsuarioCorreo(), pageable);
            } else if (tieneFechas) {
                pagina = comprasRepository.findByFechaBetween(request.getFechaInicio(), request.getFechaFin(), pageable);
            } else {
                pagina = comprasRepository.findAll(pageable);
            }

            List<CompraAdminResponse> compras = pagina.getContent().stream().map(this::mapToResponse).toList();
            response.setCompras(compras);
            response.setTotalPaginas(pagina.getTotalPages());
            response.setTotalElementos(pagina.getTotalElements());
            response.setCodigo(0);
            response.setMensaje("Compras encontradas");
        } catch (Exception e) {
            log.error("Error al listar compras admin: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al listar compras");
        }
        return response;
    }

    @Override
    public GenericResponse validarCompra(ValidarCompraRequest request) {
        GenericResponse response = new GenericResponse();
        try {
            Optional<Compras> opt = comprasRepository.findById(request.getIdCompra());
            if (opt.isEmpty()) {
                response.setCodigo(2);
                response.setMensaje("Compra no encontrada");
                return response;
            }
            Compras compra = opt.get();
            compra.setValidado(1);
            comprasRepository.save(compra);
            response.setCodigo(0);
            response.setMensaje("Compra validada correctamente");
        } catch (Exception e) {
            log.error("Error al validar compra: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al validar compra");
        }
        return response;
    }

    private String mapMetodoPago(Integer metodoPago) {
        if (metodoPago == null) return null;
        return switch (metodoPago) {
            case 0 -> "Efectivo";
            case 1 -> "Pago Electronico";
            case 2 -> "Venta a Credito";
            default -> String.valueOf(metodoPago);
        };
    }

    private CompraAdminResponse mapToResponse(Compras c) {
        CompraAdminResponse dto = new CompraAdminResponse();
        dto.setId(c.getId());
        dto.setUsuarioCorreo(c.getUsuarioCorreo());
        dto.setClaveAgente(c.getClaveAgente());
        dto.setCaptureId(c.getCaptureId());
        dto.setTotal(c.getTotal());
        dto.setClaveCliente(c.getClaveCliente());
        dto.setFecha(c.getFecha());
        dto.setDireccionEnvio(c.getDireccionEnvio());
        dto.setValidado(c.getValidado());
        dto.setMetodoPago(mapMetodoPago(c.getMetodoPago()));
        dto.setDevolucion(c.getDevolucion());
        dto.setPromocion(c.getPromocion());
        dto.setMerma(c.getMerma());
        dto.setConsecutivo(c.getConsecutivo());
        if (c.getDetalles() != null) {
            List<DetalleCompraResponse> detalles = c.getDetalles().stream().map(d -> {
                DetalleCompraResponse det = new DetalleCompraResponse();
                det.setId(d.getId());
                det.setProductoId(d.getProductoId());
                det.setCantidad(d.getCantidad());
                det.setPrecioUnitario(d.getPrecioUnitario());
                return det;
            }).toList();
            dto.setDetalles(detalles);
        }
        return dto;
    }
}
