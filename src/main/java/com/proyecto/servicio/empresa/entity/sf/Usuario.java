package com.proyecto.servicio.empresa.entity.sf;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "personas")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombre")
    private String nombre;

    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @Column(name="correo")
    private String correo;

    @Column(name = "clave_agente")
    private String claveAgente;

    @Column(name="rol")
    private String rol;
    @Column(name = "password")
    private String password;
    @Column(name = "descripcion")
    private String descripcion;
}
