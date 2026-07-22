package com.proyecto.servicio.empresa.entity.sf;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "compras")
public class Compras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "usuario_correo", nullable = false)
    private String usuarioCorreo;

    @Column(name = "clave_agente")
    private String claveAgente;

    @Column(name = "capture_id", nullable = false, unique = true)
    private String captureId;

    @Column(name = "total")
    private Double total;

    @Column(name = "clave_cliente")
    private String claveCliente;

    @Column(name = "fecha")
    private String fecha;

    @Column(name = "direccion_envio")
    private String direccionEnvio;

    @Column(name = "validado")
    private Integer validado = 0;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "metodo_pago")
    private Integer metodoPago = 0;

    @Column(name = "devolucion")
    private Integer devolucion = 0;

    @Column(name = "promocion")
    private Integer promocion = 0;

    @Column(name = "merma")
    private Integer merma = 0;

    @Column(name = "consecutivo")
    private Integer consecutivo = 0;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<DetalleCompra> detalles;
}
