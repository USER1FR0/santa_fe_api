package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Data
public class LoginRequest {
    @NotEmpty(message = "El correo no puede estar vacía")
    @NotNull(message = "El correo no puede ser nulo")
    private String correo;
    @NotEmpty(message = "El password no puede estar vacía")
    @NotNull(message = "El password no puede ser nulo")
    @Pattern(
            regexp = "^[A-Za-z0-9+/=]{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres y solo puede contener letras (A-Z, a-z), números (0-9) y los símbolos + / ="
    )
    private  String password;
}
