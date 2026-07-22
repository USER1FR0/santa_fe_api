package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ActualizaUserRequest {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    @Email
    private String nuevoCorreo;
    @Email
    @NotBlank(message = "El correo actual no puede estar vacío")
    private String correo;
    private String password;
    @Pattern(regexp = "^[123]$", message = "El campo rol solo puede ser 1, 2 o 3")
    private String rol;
    @Pattern(regexp = "^[0-9]{3,}$", message = "La clave de agente debe contener solo números y tener mínimo 3 dígitos")
    private String claveAgente;
    private String descripcion;
}
