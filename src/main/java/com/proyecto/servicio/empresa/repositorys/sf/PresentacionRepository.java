package com.proyecto.servicio.empresa.repositorys.sf;

import com.proyecto.servicio.empresa.entity.sf.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PresentacionRepository extends JpaRepository<Presentacion, Long> {
    boolean existsByGramos(Integer gramos);


    Optional<Presentacion> findByEtiqueta(String etiqueta);
}
