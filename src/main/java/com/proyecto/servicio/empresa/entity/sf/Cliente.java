package com.proyecto.servicio.empresa.entity.sf;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cliente_app")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clave_cliente", unique = true)
    private String claveCliente;

    @Column(name = "nombre_cliente")
    private String nombreCliente;
}
