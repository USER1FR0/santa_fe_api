package com.proyecto.servicio.empresa.repositorys.sf;

import com.proyecto.servicio.empresa.entity.sf.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCorreo(String correo);

    Page<Usuario> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Optional<Usuario> findByCorreo(String correo);

    @Query("SELECT u FROM Usuario u WHERE u.id IN :ids")
    List<Usuario> findByIdIn(@Param("ids") List<Long> ids);




}
