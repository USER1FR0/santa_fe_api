package com.proyecto.servicio.empresa.service;

import com.proyecto.servicio.empresa.entity.sf.Compras;
import com.proyecto.servicio.empresa.entity.sf.DetalleCompra;
import com.proyecto.servicio.empresa.model.request.SincronizarComprasRequest;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.repositorys.sf.ComprasRepository;
import com.proyecto.servicio.empresa.repositorys.sf.DetalleCompraRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ComprasService {

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Transactional
    public GenericResponse sincronizarCompras(List<SincronizarComprasRequest> comprasList) {
        GenericResponse response = new GenericResponse();
        int insertadas = 0;
        int omitidas = 0;

        try {
            for (SincronizarComprasRequest req : comprasList) {
                if (comprasRepository.existsByCaptureId(req.getCaptureId())) {
                    omitidas++;
                    log.info("Compra ya existente, omitida: {}", req.getCaptureId());
                    continue;
                }

                Compras compra = new Compras();
                compra.setUsuarioCorreo(req.getUsuarioCorreo());
                compra.setClaveAgente(req.getClaveAgente());
                compra.setCaptureId(req.getCaptureId());
                compra.setTotal(req.getTotal());
                compra.setClaveCliente(req.getClaveCliente());
                compra.setFecha(req.getFecha());
                compra.setDireccionEnvio(req.getDireccionEnvio());
                compra.setLatitud(req.getLatitud());
                compra.setLongitud(req.getLongitud());
                compra.setMetodoPago(req.getMetodoPago() != null ? req.getMetodoPago() : 0);
                compra.setDevolucion(req.getDevolucion() != null ? req.getDevolucion() : 0);
                compra.setPromocion(req.getPromocion() != null ? req.getPromocion() : 0);
                compra.setMerma(req.getMerma() != null ? req.getMerma() : 0);
                compra.setConsecutivo(req.getConsecutivo() != null ? req.getConsecutivo() : 0);
                compra.setValidado(0);

                Compras compraGuardada = comprasRepository.save(compra);

                List<DetalleCompra> detalles = new ArrayList<>();
                for (var detalleReq : req.getDetalles()) {
                    DetalleCompra detalle = new DetalleCompra();
                    detalle.setCompra(compraGuardada);
                    detalle.setProductoId(detalleReq.getProductoId());
                    detalle.setCantidad(detalleReq.getCantidad());
                    detalle.setPrecioUnitario(detalleReq.getPrecioUnitario());
                    detalles.add(detalle);
                }
                detalleCompraRepository.saveAll(detalles);

                insertadas++;
            }

            response.setCodigo(0);
            response.setMensaje("Sincronización completada. Insertadas: " + insertadas + ", omitidas (ya existían): " + omitidas);

        } catch (Exception e) {
            log.error("Error al sincronizar compras: {}", e.getMessage(), e);
            response.setCodigo(1);
            response.setMensaje("Error al sincronizar compras: " + e.getMessage());
        }

        return response;
    }
}
