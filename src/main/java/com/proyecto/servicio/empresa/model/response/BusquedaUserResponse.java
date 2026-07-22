package com.proyecto.servicio.empresa.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.proyecto.servicio.empresa.entity.sf.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusquedaUserResponse extends GenericResponse {
        private List<UsuarioResponse> usuarios;
        private int totalPaginas;
        private long totalElementos;



}
