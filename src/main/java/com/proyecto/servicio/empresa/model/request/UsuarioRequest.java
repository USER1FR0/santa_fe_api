package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
public class UsuarioRequest {
    @NotEmpty(message = "El nombre no puede estar vacía")
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;
    @NotEmpty(message = "El apellidoPaterno no puede estar vacía")
    @NotNull(message = "El apellidoPaterno no puede ser nulo")
    private String apellidoPaterno;

    @NotEmpty(message = "El apellidoMaterno no puede estar vacía")
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellidoMaterno;

    @Email
    private String correo;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(
            regexp = "^[A-Za-z0-9+/=]{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres y solo puede contener letras (A-Z, a-z), números (0-9) y los símbolos + / ="
    )
    private String password;
    @NotEmpty
    @NonNull
    @Pattern(regexp = "^[123]$", message = "El campo rol solo puede ser 1, 2 o 3")
    private String rol;

    @NotBlank(message = "La clave de agente no puede estar vacía")
    @Pattern(regexp = "^[0-9]{3,}$", message = "La clave de agente debe contener solo números y tener mínimo 3 dígitos")
    private String claveAgente;

    private String descripcion;
}