package com.proyecto.servicio.empresa.entity.sf;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "carrito_app")
public class CarritoApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoApp producto;

    private Integer cantidad;

    @Column(name = "usuario_correo", nullable = false)
    private String usuarioCorreo;

    @Column(name = "createdat", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Getters y Setters
}
