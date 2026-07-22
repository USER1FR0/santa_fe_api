package com.proyecto.servicio.empresa.repositorys.sf;

import com.proyecto.servicio.empresa.entity.sf.ProductoApp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoAppRepository extends JpaRepository<ProductoApp, Long> {

    boolean existsByNombreProduct(String producto);
    Page<ProductoApp> findByNombreProductContainingIgnoreCase(String nombre, Pageable pageable);


    Optional<ProductoApp> findByNombreProduct(String producto);
}
