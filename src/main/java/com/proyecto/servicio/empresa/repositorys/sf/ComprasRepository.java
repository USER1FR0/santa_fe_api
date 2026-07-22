package com.proyecto.servicio.empresa.repositorys.sf;

import com.proyecto.servicio.empresa.entity.sf.Compras;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprasRepository extends JpaRepository<Compras, Long> {
    boolean existsByCaptureId(String captureId);
    Page<Compras> findByUsuarioCorreo(String usuarioCorreo, Pageable pageable);
    Page<Compras> findByFechaBetween(String fechaInicio, String fechaFin, Pageable pageable);
    Page<Compras> findByUsuarioCorreoAndFechaBetween(String usuarioCorreo, String fechaInicio, String fechaFin, Pageable pageable);
}
