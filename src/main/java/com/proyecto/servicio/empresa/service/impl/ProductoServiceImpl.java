package com.proyecto.servicio.empresa.service.impl;

import com.proyecto.servicio.empresa.entity.sf.Presentacion;
import com.proyecto.servicio.empresa.entity.sf.ProductoApp;
import com.proyecto.servicio.empresa.entity.sf.UserProduct;
import com.proyecto.servicio.empresa.entity.sf.Usuario;
import com.proyecto.servicio.empresa.model.request.ActualizarProductoRequest;
import com.proyecto.servicio.empresa.model.request.BuscarProductoRequest;
import com.proyecto.servicio.empresa.model.request.DesasignarProductoRequest;
import com.proyecto.servicio.empresa.model.request.EliminarProductoRequest;
import com.proyecto.servicio.empresa.model.request.ListarProductosAsignadosRequest;
import com.proyecto.servicio.empresa.model.request.ProductoAsignadoRequest;
import com.proyecto.servicio.empresa.model.request.ProductoRequest;
import com.proyecto.servicio.empresa.model.response.BusquedaProductoResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.model.response.ProductoAsignadoResponse;
import com.proyecto.servicio.empresa.model.response.ProductoResponse;
import com.proyecto.servicio.empresa.model.response.ProductosAsignadosResponse;
import com.proyecto.servicio.empresa.repositorys.sf.PresentacionRepository;
import com.proyecto.servicio.empresa.repositorys.sf.ProductoAppRepository;
import com.proyecto.servicio.empresa.repositorys.sf.UserProductRepository;
import com.proyecto.servicio.empresa.repositorys.sf.UsuarioRepository;
import com.proyecto.servicio.empresa.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoAppRepository productoAppRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UserProductRepository userProductRepository;
    @Autowired
    private PresentacionRepository presentacionRepository;

    @Override
    public ProductoResponse agregarProducto(ProductoRequest productoRequest) {
       ProductoResponse productoResponse = new ProductoResponse();
       try {
           boolean isProducto=false;
           isProducto=productoAppRepository.existsByNombreProduct(productoRequest.getNombreProduct());
           if(isProducto){
               productoResponse.setCodigo(2);
               productoResponse.setMensaje("El producto ya existe");
               return productoResponse;
           }

           ProductoApp productoApp = new ProductoApp();
           BeanUtils.copyProperties(productoRequest, productoApp);
           productoApp.setClaveProduct(productoRequest.getClaveProduct());
           productoApp.setLugar(productoRequest.getLugar());
           productoApp.setClaveSucursal(productoRequest.getClaveSucursal());
           productoApp.setClaveMoneda(productoRequest.getClaveMoneda());
           productoApp.setClaveMonD(productoRequest.getClaveMonD());
           productoApp.setUnidad(productoRequest.getUnidad());
           productoApp.setPresentacionId(productoRequest.getPresentacionId());
           productoAppRepository.save(productoApp);
           BeanUtils.copyProperties(productoApp, productoResponse);
           productoResponse.setCodigo(0);
           productoResponse.setMensaje("Producto agregado exitosamente");
           return productoResponse;

       }catch (Exception e){
           log.error(e.getMessage());
           productoResponse.setCodigo(1);
           productoResponse.setMensaje("Error al agregar producto");
           return productoResponse;
       }
    }

    @Override
    public ProductoResponse actualizarProducto(ActualizarProductoRequest request) {
        ProductoResponse productoResponse = new ProductoResponse();
        try {
//            boolean isProducto=false;
//            isProducto=productoAppRepository.existsByNombreProduct(request.getNombreProduct());
//            if(isProducto){
//                productoResponse.setCodigo(2);
//                productoResponse.setMensaje("El nombre  producto ya existe");
//                return productoResponse;
//            }
            List<Presentacion> presentacions=presentacionRepository.findAll();
            Optional<ProductoApp> productoApp=productoAppRepository.findById(Long.valueOf(request.getIdProducto()));
            if(productoApp.isPresent()){
                ProductoApp productoAppOptional = productoApp.get();
                productoAppOptional.setCantidadProducto(request.getCantidadProducto()!=null? request.getCantidadProducto() : productoAppOptional.getCantidadProducto());
                productoAppOptional.setPrecio(request.getPrecio()!=null?request.getPrecio():productoAppOptional.getPrecio());
                Presentacion pre = presentacions.stream()
                        .filter(presentacion ->
                                presentacion.getEtiqueta().equalsIgnoreCase(request.getPresentacionId()))
                        .findFirst()
                        .orElse(null);
               productoAppOptional.setPresentacionId(pre.getId());
                productoAppRepository.save(productoAppOptional);
                BeanUtils.copyProperties(productoAppOptional, productoResponse);
                productoResponse.setIdProducto(productoAppOptional.getId());
                productoResponse.setCodigo(0);
                productoResponse.setMensaje("Producto actualizado exitosamente");
                return productoResponse;
            }else {
                productoResponse.setCodigo(1);
                productoResponse.setMensaje("Producto no existe");
                return  productoResponse;
            }



        }catch (Exception e){
            log.error(e.getMessage());
            productoResponse.setCodigo(1);
            productoResponse.setMensaje("Error al agregar producto");
            return productoResponse;
        }
    }

    @Override
    public BusquedaProductoResponse buscarProductoPorId(BuscarProductoRequest request) {
        BusquedaProductoResponse busquedaResponse = new BusquedaProductoResponse();
        try {
        Pageable pageable = PageRequest.of(
                Math.toIntExact(request.getPagina()),
                Math.toIntExact(request.getTamanoPagina())
        );

        Page<ProductoApp> productoApps = productoAppRepository.findByNombreProductContainingIgnoreCase(
                request.getNombreProducto(), pageable
        );

        if (productoApps.isEmpty()) {
            busquedaResponse.setMensaje("No existe información del producto");
            busquedaResponse.setCodigo(3);
            return busquedaResponse;
        }

        List<ProductoResponse> productoResponses = productoApps.getContent().stream().map(productoApp -> {
            ProductoResponse dto = new ProductoResponse();
            dto.setIdProducto(productoApp.getId());
            dto.setClaveProduct(productoApp.getClaveProduct());
            dto.setNombreProduct(productoApp.getNombreProduct());
            dto.setPrecio(productoApp.getPrecio());
            dto.setCantidadProducto(productoApp.getCantidadProducto());
            dto.setImagen(productoApp.getImagen());
            dto.setLugar(productoApp.getLugar());
            dto.setClaveSucursal(productoApp.getClaveSucursal());
            dto.setClaveMoneda(productoApp.getClaveMoneda());
            dto.setClaveMonD(productoApp.getClaveMonD());
            dto.setUnidad(productoApp.getUnidad());
            Optional<Presentacion> opt = presentacionRepository.findById(productoApp.getPresentacionId()!=null?productoApp.getPresentacionId(): 1l);
            if(opt.isPresent()){
                Presentacion presentacion=opt.get();
                dto.setPresentacionId(presentacion.getEtiqueta());
            }

            return dto;
        }).toList();
        busquedaResponse.setProductoApps(productoResponses);
        busquedaResponse.setTotalPaginas(productoApps.getTotalPages());
        busquedaResponse.setTotalElementos(productoApps.getTotalElements());
        busquedaResponse.setMensaje("Productos encontrados correctamente");
        busquedaResponse.setCodigo(0);
    }catch (Exception e){
        log.error("Ha ocurrido un error al consultar los productos a causa de :",e);
        busquedaResponse.setCodigo(3);
        busquedaResponse.setMensaje("Error al buscar productos");
        return  busquedaResponse;
    }

        return busquedaResponse;
    }

    @Override
    @Transactional
    public ProductoAsignadoResponse asignarProducto(ProductoAsignadoRequest request) {
        ProductoAsignadoResponse productoResponse = new ProductoAsignadoResponse();

        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            productoResponse.setCodigo(1);
            productoResponse.setMensaje("La cantidad debe ser mayor a cero");
            return productoResponse;
        }

        Optional<Usuario> userOpt = usuarioRepository.findByCorreo(request.getCorreo());
        if (!userOpt.isEmpty()&&userOpt.get().getRol().equals("1")) {
            productoResponse.setCodigo(1);
            productoResponse.setMensaje("Usuario no existe");
            return productoResponse;
        }

        Optional<ProductoApp> productoOpt = request.getProductoId() != null
                ? productoAppRepository.findById(request.getProductoId())
                : productoAppRepository.findByNombreProduct(request.getNombreProducto());
        if (productoOpt.isEmpty()) {
            productoResponse.setCodigo(1);
            productoResponse.setMensaje("Producto no existe");
            return productoResponse;
        }

        Usuario usuario = userOpt.get();
        ProductoApp producto = productoOpt.get();

        if (request.getCantidad() > producto.getCantidadProducto()) {
            productoResponse.setCodigo(1);
            productoResponse.setMensaje("Stock insuficiente: disponible " + producto.getCantidadProducto());
            return productoResponse;
        }

        LocalDateTime hoy = LocalDateTime.now();
        Optional<UserProduct> existenteHoy = userProductRepository
                .findByUserIdAndProductoIdAndDate(usuario.getId(), producto.getId(), hoy);

        UserProduct userProduct;
        if (existenteHoy.isPresent()) {
            // Ya existe registro del día: acumular cantidad; initial_quantity no se modifica
            userProduct = existenteHoy.get();
            userProduct.setCantidad(userProduct.getCantidad() + request.getCantidad());
            userProduct.setAssignedAt(hoy);
        } else {
            // Primer registro del día para este (usuario, producto)
            userProduct = new UserProduct();
            userProduct.setUserId(usuario.getId());
            userProduct.setProducto(producto);
            userProduct.setCantidad(request.getCantidad());
            userProduct.setInitialQuantity(request.getCantidad());
            userProduct.setAssignedAt(hoy);
        }

        producto.setCantidadProducto(producto.getCantidadProducto() - request.getCantidad());
        productoAppRepository.save(producto);
        userProductRepository.save(userProduct);

        productoResponse.setCodigo(0);
        productoResponse.setMensaje("Producto asignado exitosamente");
        productoResponse.setCorreo(request.getCorreo());
        productoResponse.setNombreProducto(producto.getNombreProduct());
        productoResponse.setCantidad(userProduct.getCantidad());
        return productoResponse;
    }

    @Override
    @Transactional
    public GenericResponse desasignarProducto(DesasignarProductoRequest request) {
        GenericResponse response = new GenericResponse();
        try {
            Optional<UserProduct> userProductOpt = userProductRepository.findById(request.getUserProductId());
            if (userProductOpt.isEmpty()) {
                response.setCodigo(2);
                response.setMensaje("Asignación no encontrada");
                return response;
            }

            Optional<ProductoApp> productoOpt = productoAppRepository.findById(request.getProductoId());
            if (productoOpt.isEmpty()) {
                response.setCodigo(2);
                response.setMensaje("Producto no encontrado");
                return response;
            }

            UserProduct userProduct = userProductOpt.get();
            ProductoApp producto = productoOpt.get();

            // Devolver el stock al almacén
            producto.setCantidadProducto(producto.getCantidadProducto() + request.getCantidad());
            productoAppRepository.save(producto);
            userProductRepository.delete(userProduct);

            response.setCodigo(0);
            response.setMensaje("Producto desasignado correctamente");
        } catch (Exception e) {
            log.error("Error al desasignar producto: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al desasignar producto");
        }
        return response;
    }

    @Override
    public ProductosAsignadosResponse listarProductosAsignados(ListarProductosAsignadosRequest request) {
        ProductosAsignadosResponse response = new ProductosAsignadosResponse();
        try {
            Optional<Usuario> userOpt = usuarioRepository.findByCorreo(request.getCorreo());
            if (userOpt.isEmpty()) {
                response.setCodigo(1);
                response.setMensaje("Usuario no encontrado");
                return response;
            }

            List<UserProduct> asignados = userProductRepository.findByUserId(userOpt.get().getId());

            List<ProductosAsignadosResponse.ProductoAsignadoItem> items = asignados.stream().map(up -> {
                ProductosAsignadosResponse.ProductoAsignadoItem item = new ProductosAsignadosResponse.ProductoAsignadoItem();
                item.setAssignedId(up.getId());
                item.setUserId(up.getUserId());
                item.setProductId(up.getProducto().getId());
                item.setNombreProduct(up.getProducto().getNombreProduct());
                item.setCorreo(request.getCorreo());
                item.setCantidad(up.getCantidad());
                item.setInitialQuantity(up.getInitialQuantity());
                return item;
            }).toList();

            response.setProductos(items);
            response.setCodigo(0);
            response.setMensaje("Productos asignados encontrados");
        } catch (Exception e) {
            log.error("Error al listar productos asignados: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al listar productos asignados");
        }
        return response;
    }

    @Override
    public GenericResponse eliminarProducto(EliminarProductoRequest request) {
        GenericResponse response = new GenericResponse();
        try {
            Optional<ProductoApp> opt = productoAppRepository.findById(request.getIdProducto());
            if (opt.isEmpty()) {
                response.setCodigo(2);
                response.setMensaje("Producto no encontrado");
                return response;
            }
            productoAppRepository.delete(opt.get());
            response.setCodigo(0);
            response.setMensaje("Producto eliminado correctamente");
        } catch (Exception e) {
            log.error("Error al eliminar producto: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al eliminar producto");
        }
        return response;
    }

    private  int actualizarValor(int valorBD, int valorRequest) {
        int resultado = valorBD;

        if (valorRequest >0) {
            resultado += valorRequest; // suma
        } else{
            resultado -= valorRequest; // resta
        }

        return resultado;
    }
}
