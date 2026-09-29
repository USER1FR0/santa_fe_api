package com.proyecto.servicio.empresa.service;

import com.proyecto.servicio.empresa.entity.sf.Compras;
import com.proyecto.servicio.empresa.entity.sf.DetalleCompra;
import com.proyecto.servicio.empresa.entity.sf.UserProduct;
import com.proyecto.servicio.empresa.entity.sf.Usuario;
import com.proyecto.servicio.empresa.model.request.SincronizarComprasRequest;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.repositorys.sf.ComprasRepository;
import com.proyecto.servicio.empresa.repositorys.sf.DetalleCompraRepository;
import com.proyecto.servicio.empresa.repositorys.sf.UserProductRepository;
import com.proyecto.servicio.empresa.repositorys.sf.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ComprasService {

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private UserProductRepository userProductRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public GenericResponse sincronizarCompras(List<SincronizarComprasRequest> comprasList) {
        GenericResponse response = new GenericResponse();
        int insertadas = 0;
        int omitidas = 0;

        try {
            Map<String, Integer> consecutivoPorUsuario = new HashMap<>();

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
                compra.setValidado(0);

                Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(req.getUsuarioCorreo());
                if (usuarioOpt.isPresent() && usuarioOpt.get().getClaveSucursal() != null
                        && !usuarioOpt.get().getClaveSucursal().isBlank()) {
                    int siguiente = consecutivoPorUsuario.computeIfAbsent(
                            req.getUsuarioCorreo(),
                            correo -> comprasRepository.findMaxConsecutivoByUsuarioCorreo(correo) + 1
                    );
                    compra.setConsecutivo(siguiente);
                    consecutivoPorUsuario.put(req.getUsuarioCorreo(), siguiente + 1);
                } else {
                    compra.setConsecutivo(0);
                }

                Compras compraGuardada = comprasRepository.save(compra);

                List<DetalleCompra> detalles = new ArrayList<>();
                for (var detalleReq : req.getDetalles()) {
                    DetalleCompra detalle = new DetalleCompra();
                    detalle.setCompra(compraGuardada);
                    detalle.setProductoId(detalleReq.getProductoId());
                    detalle.setCantidad(detalleReq.getCantidad());
                    detalle.setPrecioUnitario(detalleReq.getPrecioUnitario());
                    detalles.add(detalle);
                    descuentaProducto(req.getUsuarioCorreo(),detalle.getProductoId(),detalle.getCantidad());
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

    private void descuentaProducto(String correo, Long idProducto, Integer cantidad) {

        if (correo == null) {
            return;
        }

        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);

        if (usuario.isEmpty()) {
            return;
        }

        Optional<UserProduct> producto =
                userProductRepository.findByUserIdAndProductoId(
                        usuario.get().getId(),
                        idProducto);

        if (producto.isEmpty()) {
            return;
        }

        UserProduct product = producto.get();

        if (product.getCantidad() < cantidad) {
            throw new IllegalArgumentException(
                    "Inventario insuficiente");
        }

        product.setCantidad(product.getCantidad() - cantidad);
        log.info("Descuenta producto del usuario:{}",usuario.get().getCorreo());
        userProductRepository.save(product);
    }
}
