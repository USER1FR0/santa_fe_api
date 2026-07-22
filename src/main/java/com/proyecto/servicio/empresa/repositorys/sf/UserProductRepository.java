package com.proyecto.servicio.empresa.repositorys.sf;

import com.proyecto.servicio.empresa.entity.sf.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserProductRepository extends JpaRepository<UserProduct, Long> {

    List<UserProduct> findByUserId(Long userId);

    @Query("SELECT up FROM UserProduct up WHERE up.userId = :userId AND up.producto.id = :productId AND FUNCTION('DATE', up.assignedAt) = FUNCTION('DATE', :hoy)")
    Optional<UserProduct> findByUserIdAndProductoIdAndDate(
            @Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("hoy") LocalDateTime hoy
    );

    @Query("SELECT up FROM UserProduct up WHERE up.userId = :userId AND up.producto.id = :productId")
    Optional<UserProduct> findByUserIdAndProductoId(
            @Param("userId") Long userId,
            @Param("productId") Long productId
    );
}
