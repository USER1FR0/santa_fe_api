package com.proyecto.servicio.empresa.entity.sf;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "producto_app")
public class ProductoApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clave_product")
    private String claveProduct;

    @Column(name = "nombre_product")
    private String nombreProduct;

    private Double precio;

    @Column(name = "cantidad_producto")
    private Integer cantidadProducto;

    private String imagen;

    @Column(name = "lugar")
    private String lugar;

    @Column(name = "clave_sucursal")
    private String claveSucursal;

    @Column(name = "clave_moneda")
    private String claveMoneda;

    @Column(name = "clave_mon_d")
    private String claveMonD;

    @Column(name = "unidad")
    private String unidad;

    @Column(name = "presentacion_id")
    private Long presentacionId;

    @Column(name = "createdat", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "producto")
    private List<CarritoApp> carritos;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
