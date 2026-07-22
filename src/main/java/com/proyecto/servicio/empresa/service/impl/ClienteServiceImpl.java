package com.proyecto.servicio.empresa.service.impl;

import com.proyecto.servicio.empresa.entity.sf.Cliente;
import com.proyecto.servicio.empresa.model.request.ActualizarClienteRequest;
import com.proyecto.servicio.empresa.model.request.ClienteRequest;
import com.proyecto.servicio.empresa.model.request.EliminarClienteRequest;
import com.proyecto.servicio.empresa.model.response.BusquedaClienteResponse;
import com.proyecto.servicio.empresa.model.response.ClienteResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.repositorys.sf.ClienteRepository;
import com.proyecto.servicio.empresa.service.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public ClienteResponse crearCliente(ClienteRequest request) {
        ClienteResponse response = new ClienteResponse();
        try {
            if (clienteRepository.existsByClaveCliente(request.getClaveCliente())) {
                response.setCodigo(2);
                response.setMensaje("Ya existe un cliente con esa clave");
                return response;
            }
            Cliente cliente = new Cliente();
            cliente.setClaveCliente(request.getClaveCliente());
            cliente.setNombreCliente(request.getNombreCliente());
            clienteRepository.save(cliente);
            response.setId(cliente.getId());
            response.setClaveCliente(cliente.getClaveCliente());
            response.setNombreCliente(cliente.getNombreCliente());
            response.setCodigo(0);
            response.setMensaje("Cliente creado correctamente");
        } catch (Exception e) {
            log.error("Error al crear cliente: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al crear cliente");
        }
        return response;
    }

    @Override
    public ClienteResponse actualizarCliente(ActualizarClienteRequest request) {
        ClienteResponse response = new ClienteResponse();
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findById(request.getId());
            if (clienteOpt.isEmpty()) {
                response.setCodigo(2);
                response.setMensaje("Cliente no encontrado");
                return response;
            }
            Cliente cliente = clienteOpt.get();
            if (request.getClaveCliente() != null) {
                cliente.setClaveCliente(request.getClaveCliente());
            }
            if (request.getNombreCliente() != null) {
                cliente.setNombreCliente(request.getNombreCliente());
            }
            clienteRepository.save(cliente);
            response.setId(cliente.getId());
            response.setClaveCliente(cliente.getClaveCliente());
            response.setNombreCliente(cliente.getNombreCliente());
            response.setCodigo(0);
            response.setMensaje("Cliente actualizado correctamente");
        } catch (Exception e) {
            log.error("Error al actualizar cliente: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al actualizar cliente");
        }
        return response;
    }

    @Override
    public GenericResponse eliminarCliente(EliminarClienteRequest request) {
        GenericResponse response = new GenericResponse();
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findById(request.getId());
            if (clienteOpt.isEmpty()) {
                response.setCodigo(2);
                response.setMensaje("Cliente no encontrado");
                return response;
            }
            clienteRepository.delete(clienteOpt.get());
            response.setCodigo(0);
            response.setMensaje("Cliente eliminado correctamente");
        } catch (Exception e) {
            log.error("Error al eliminar cliente: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al eliminar cliente");
        }
        return response;
    }

    @Override
    public BusquedaClienteResponse listarClientes() {
        BusquedaClienteResponse response = new BusquedaClienteResponse();
        try {
            List<ClienteResponse> clientes = clienteRepository.findAll().stream().map(c -> {
                ClienteResponse dto = new ClienteResponse();
                dto.setId(c.getId());
                dto.setClaveCliente(c.getClaveCliente());
                dto.setNombreCliente(c.getNombreCliente());
                return dto;
            }).toList();
            response.setClientes(clientes);
            response.setTotalElementos((long) clientes.size());
            response.setTotalPaginas(1);
            response.setCodigo(0);
            response.setMensaje("Clientes encontrados");
        } catch (Exception e) {
            log.error("Error al listar clientes: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al listar clientes");
        }
        return response;
    }
}
