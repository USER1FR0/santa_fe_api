package com.proyecto.servicio.empresa.entity.sf;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "presentaciones")
public class Presentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gramos")
    private Integer gramos;

    @Column(name = "etiqueta")
    private String etiqueta;

    @Column(name = "activo")
    private Boolean activo = true;
}
