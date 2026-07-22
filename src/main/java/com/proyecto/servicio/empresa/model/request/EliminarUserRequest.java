package com.proyecto.servicio.empresa.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class EliminarUserRequest {
    @NotEmpty(message = "El correo no puede estar vacía")
    @NotNull(message = "El correo no puede ser nulo")
    private String correo;

}
