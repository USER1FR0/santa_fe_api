package com.proyecto.servicio.empresa.service.impl;

import com.proyecto.servicio.empresa.entity.sf.Presentacion;
import com.proyecto.servicio.empresa.model.request.ActualizarPresentacionRequest;
import com.proyecto.servicio.empresa.model.request.EliminarPresentacionRequest;
import com.proyecto.servicio.empresa.model.request.PresentacionRequest;
import com.proyecto.servicio.empresa.model.response.BusquedaPresentacionResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.model.response.PresentacionResponse;
import com.proyecto.servicio.empresa.repositorys.sf.PresentacionRepository;
import com.proyecto.servicio.empresa.service.PresentacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PresentacionServiceImpl implements PresentacionService {

    @Autowired
    private PresentacionRepository presentacionRepository;

    @Override
    public PresentacionResponse crearPresentacion(PresentacionRequest request) {
        PresentacionResponse response = new PresentacionResponse();
        try {
            if (presentacionRepository.existsByGramos(request.getGramos())) {
                response.setCodigo(2);
                response.setMensaje("Ya existe una presentación con ese gramaje");
                return response;
            }
            Presentacion presentacion = new Presentacion();
            presentacion.setGramos(request.getGramos());
            String etiqueta = request.getEtiqueta() != null ? request.getEtiqueta() : request.getGramos() + "g";
            presentacion.setEtiqueta(etiqueta);
            presentacion.setActivo(request.getActivo() != null ? request.getActivo() : true);
            presentacionRepository.save(presentacion);
            mapToResponse(presentacion, response);
            response.setCodigo(0);
            response.setMensaje("Presentación creada correctamente");
        } catch (Exception e) {
            log.error("Error al crear presentación: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al crear presentación");
        }
        return response;
    }

    @Override
    public PresentacionResponse actualizarPresentacion(ActualizarPresentacionRequest request) {
        PresentacionResponse response = new PresentacionResponse();
        try {
            Optional<Presentacion> opt = presentacionRepository.findById(request.getId());
            if (opt.isEmpty()) {
                response.setCodigo(2);
                response.setMensaje("Presentación no encontrada");
                return response;
            }
            Presentacion presentacion = opt.get();
            if (request.getGramos() != null) presentacion.setGramos(request.getGramos());
            if (request.getEtiqueta() != null) presentacion.setEtiqueta(request.getEtiqueta());
            if (request.getActivo() != null) presentacion.setActivo(request.getActivo());
            presentacionRepository.save(presentacion);
            mapToResponse(presentacion, response);
            response.setCodigo(0);
            response.setMensaje("Presentación actualizada correctamente");
        } catch (Exception e) {
            log.error("Error al actualizar presentación: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al actualizar presentación");
        }
        return response;
    }

    @Override
    public GenericResponse eliminarPresentacion(EliminarPresentacionRequest request) {
        GenericResponse response = new GenericResponse();
        try {
            Optional<Presentacion> opt = presentacionRepository.findById(request.getId());
            if (opt.isEmpty()) {
                response.setCodigo(2);
                response.setMensaje("Presentación no encontrada");
                return response;
            }
            presentacionRepository.delete(opt.get());
            response.setCodigo(0);
            response.setMensaje("Presentación eliminada correctamente");
        } catch (Exception e) {
            log.error("Error al eliminar presentación: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al eliminar presentación");
        }
        return response;
    }

    @Override
    public BusquedaPresentacionResponse listarPresentaciones(boolean soloActivas) {
        BusquedaPresentacionResponse response = new BusquedaPresentacionResponse();
        try {
            List<Presentacion> lista = presentacionRepository.findAll();
            List<PresentacionResponse> presentaciones = lista.stream()
                    .filter(p -> !soloActivas || Boolean.TRUE.equals(p.getActivo()))
                    .map(p -> {
                        PresentacionResponse dto = new PresentacionResponse();
                        mapToResponse(p, dto);
                        return dto;
                    }).toList();
            response.setPresentaciones(presentaciones);
            response.setCodigo(0);
            response.setMensaje("Presentaciones encontradas");
        } catch (Exception e) {
            log.error("Error al listar presentaciones: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al listar presentaciones");
        }
        return response;
    }

    private void mapToResponse(Presentacion p, PresentacionResponse dto) {
        dto.setId(p.getId());
        dto.setGramos(p.getGramos());
        dto.setEtiqueta(p.getEtiqueta());
        dto.setActivo(p.getActivo());
    }
}
