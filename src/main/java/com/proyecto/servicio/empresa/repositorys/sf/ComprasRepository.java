package com.proyecto.servicio.empresa.repositorys.sf;

import com.proyecto.servicio.empresa.entity.sf.Compras;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComprasRepository extends JpaRepository<Compras, Long> {
    boolean existsByCaptureId(String captureId);
    Page<Compras> findByUsuarioCorreo(String usuarioCorreo, Pageable pageable);
    Page<Compras> findByFechaBetween(String fechaInicio, String fechaFin, Pageable pageable);
    Page<Compras> findByUsuarioCorreoAndFechaBetween(String usuarioCorreo, String fechaInicio, String fechaFin, Pageable pageable);

    List<Compras> findByFechaBetween(String fechaInicio, String fechaFin);
    List<Compras> findByUsuarioCorreo(String usuarioCorreo);
    List<Compras> findByUsuarioCorreoAndFechaBetween(String usuarioCorreo, String fechaInicio, String fechaFin);

    @Query("SELECT COALESCE(MAX(c.consecutivo), 0) FROM Compras c WHERE c.usuarioCorreo = :correo")
    Integer findMaxConsecutivoByUsuarioCorreo(@Param("correo") String correo);
}
