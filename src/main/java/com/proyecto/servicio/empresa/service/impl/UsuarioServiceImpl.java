package com.proyecto.servicio.empresa.service.impl;

import com.proyecto.servicio.empresa.entity.sf.Usuario;
import com.proyecto.servicio.empresa.model.request.*;
import com.proyecto.servicio.empresa.model.response.BusquedaUserResponse;
import com.proyecto.servicio.empresa.model.response.GenericResponse;
import com.proyecto.servicio.empresa.model.response.LoginResponse;
import com.proyecto.servicio.empresa.model.response.UsuarioResponse;
import com.proyecto.servicio.empresa.repositorys.sf.UsuarioRepository;
import com.proyecto.servicio.empresa.service.UsuarioService;
import com.proyecto.servicio.empresa.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public GenericResponse eliminarUser(EliminarUserRequest eliminarUserRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Optional<Usuario> usuario = usuarioRepository.findByCorreo(eliminarUserRequest.getCorreo());
            if(usuario.isPresent()){
                Usuario usuarioElimar = usuario.get();
                usuarioRepository.delete(usuarioElimar);
                genericResponse.setCodigo(0);
                genericResponse.setMensaje("Usuario eliminado correctamente");
                return genericResponse;
            }else{
                genericResponse.setCodigo(1);
                genericResponse.setMensaje("Usuario no encontrado");
                return genericResponse;
            }

        } catch (Exception e) {
            genericResponse.setCodigo(1);
            genericResponse.setMensaje("Error al eliminar usuario");
            log.error("Error al eliminar usuario, a causa de:{}", e);
            return  genericResponse;

        }

    }

    @Override
    public UsuarioResponse registrarUsuario(UsuarioRequest request) {
        UsuarioResponse usuarioResponse = new UsuarioResponse();
        try {
            boolean isUserExist = false;
            isUserExist = usuarioRepository.existsByCorreo(request.getCorreo());
            if (isUserExist) {
                usuarioResponse.setCodigo(2);
                usuarioResponse.setMensaje("Usuario ya existe");
            } else {
                Usuario usuario = new Usuario();
                usuario.setPassword(encrypt(request.getPassword()));
                usuario.setNombre(request.getNombre());
                usuario.setCorreo(request.getCorreo());
                usuario.setRol(request.getRol());
                usuario.setDescripcion(request.getDescripcion());
                usuario.setApellidoPaterno(request.getApellidoPaterno());
                usuario.setApellidoMaterno(request.getApellidoMaterno());
                usuario.setClaveAgente(request.getClaveAgente());
                usuarioResponse.setCodigo(0);
                usuarioResponse.setMensaje("Usuario registrado");
                usuarioRepository.save(usuario);

            }
        }catch (Exception e){
            log.error("Ha ocurrido un error al agregar usuarios:{}",e.getMessage());
            usuarioResponse.setCodigo(2);
            usuarioResponse.setMensaje("Error al agregar usuarios");
            return usuarioResponse;
        }
        return usuarioResponse;
    }
    protected static String decrypt(String strToDecrypt) {
        String secretKey1 = "5TpM3x2021paL~nX!D.^$";
        String salt = "S4l7202157pm3x<bbX&";
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            if (strToDecrypt.isEmpty() || strToDecrypt == null || strToDecrypt.equals("")) {
                return "error al desencriptar";
            }
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey1.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            byte[] decoded = Base64.getDecoder().decode(strToDecrypt);
            String desencriptado = new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
            log.info("Texto desencriptado: [{}]", desencriptado);
            return desencriptado;

        } catch (Exception e) {
            //log.error("Error while decrypting: " + e.toString());
            System.out.printf("Error: %s\n", e.getMessage());
        }
        return null;
    }

    private static String encrypt(String strToEncrypt) {
        try {
            String secretKey1 = "5TpM3x2021paL~nX!D.^$";
            String salt = "S4l7202157pm3x<bbX&";
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec1 = new IvParameterSpec(iv);
            if (strToEncrypt.isEmpty() || strToEncrypt == null || strToEncrypt.equals("")) {
                return "error al cifrar cadena";
            }

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey1.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec1);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            log.error("Error while encrypting: " + e.toString());
        }
        return null;
    }

    @Override
    public UsuarioResponse actualizarUsuario(ActualizaUserRequest request) {
     UsuarioResponse usuarioResponse = new UsuarioResponse();
     try {
         boolean isCorreo=false;
         isCorreo=usuarioRepository.existsByCorreo(request.getNuevoCorreo());
         if (isCorreo) {
             usuarioResponse.setCodigo(2);
             usuarioResponse.setMensaje("Ya existe un usuario con el correo->"+request.getNuevoCorreo());
             return usuarioResponse;
         }

         Optional<Usuario> user = usuarioRepository.findByCorreo(request.getCorreo());
         if (user.isPresent()) {
             Usuario userOptional = user.get();
             userOptional.setPassword(request.getPassword()!=null?encrypt(request.getPassword()):userOptional.getPassword());
             userOptional.setNombre(request.getNombre()!=null?request.getNombre():userOptional.getNombre());
             userOptional.setCorreo(request.getNuevoCorreo()!=null?request.getNuevoCorreo():userOptional.getCorreo());
             userOptional.setRol(request.getRol()!=null?request.getRol():userOptional.getRol());
             userOptional.setApellidoMaterno(request.getApellidoMaterno()!=null?request.getApellidoMaterno():userOptional.getApellidoMaterno());
             userOptional.setApellidoPaterno(request.getApellidoPaterno()!=null?request.getApellidoPaterno():userOptional.getApellidoPaterno());
             userOptional.setClaveAgente(request.getClaveAgente()!=null?request.getClaveAgente():userOptional.getClaveAgente());
             usuarioRepository.save(userOptional);
             usuarioResponse.setMensaje("Usuario actualizado");
             usuarioResponse.setCodigo(0);
         } else {
             usuarioResponse.setCodigo(2);
             usuarioResponse.setMensaje("Usuario no existe");
         }
     }catch (Exception e){
         usuarioResponse.setCodigo(3);
         usuarioResponse.setMensaje("Error al actualizar usuarios");
         log.error("Ha ocurrido un error al actualizar usuarios a causa de :{}",e.getMessage());
     }
     return usuarioResponse;
    }

    @Override
    public BusquedaUserResponse buscarUsuario(BuscarUserRequest request) {
        BusquedaUserResponse busquedaResponse = new BusquedaUserResponse();
        try {

            Pageable pageable = PageRequest.of(
                    Math.toIntExact(request.getPagina()),
                    Math.toIntExact(request.getTamanoPagina())
            );

            Page<Usuario> usuarios = usuarioRepository.findByNombreContainingIgnoreCase(
                    request.getNombre(), pageable
            );

            if (usuarios.isEmpty()) {
                busquedaResponse.setMensaje("No existe información del usuario");
                busquedaResponse.setCodigo(3);
                return busquedaResponse;
            }

            // ✅ Convertimos los usuarios de entidad a DTO (usando getContent())
            List<UsuarioResponse> usuarioResponses = usuarios.getContent().stream().map(usuario -> {
                UsuarioResponse dto = new UsuarioResponse();
                String desencriptar=decrypt(usuario.getPassword());
                dto.setPassword(desencriptar);
                dto.setNombre(usuario.getNombre());
                dto.setRol(usuario.getRol());
                dto.setApellidoMaterno(usuario.getApellidoMaterno());
                dto.setApellidoPaterno(usuario.getApellidoPaterno());
                dto.setCorreo(usuario.getCorreo());
                dto.setDescripcion(usuario.getDescripcion());
                dto.setClaveAgente(usuario.getClaveAgente());
                return dto;
            }).toList();

            // ✅ Llenamos la respuesta
            busquedaResponse.setUsuarios(usuarioResponses);
            busquedaResponse.setTotalPaginas(usuarios.getTotalPages());
            busquedaResponse.setTotalElementos(usuarios.getTotalElements());
            busquedaResponse.setMensaje("Usuarios encontrados correctamente");
            busquedaResponse.setCodigo(0);
        }catch (Exception e){
            log.error("Ha ocurrido un error al consultar los usuarios a causa de :{}",e.getMessage());
            busquedaResponse.setCodigo(3);
            busquedaResponse.setMensaje("Error al buscar usuarios");
            return  busquedaResponse;
        }

        return busquedaResponse;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
       LoginResponse loginResponse = new LoginResponse();
       log.info("consume servicio login");
       Optional<Usuario> user=usuarioRepository.findByCorreo(request.getCorreo());
       if(user.isPresent()){
           Usuario userValida=user.get();
           String token = jwtTokenUtil.generateToken(request.getCorreo());

          String pass=decrypt(userValida.getPassword());
           if(pass.equals(request.getPassword())){
               loginResponse.setCorreo(userValida.getCorreo());
               boolean isAdmin = userValida.getRol().equals("1");
               loginResponse.setAdmin(isAdmin);
               loginResponse.setMensaje("Datos validos correctamente");
               loginResponse.setCodigo(0);
               return loginResponse;
           }else{
               loginResponse.setMensaje("Incorrect password");
               loginResponse.setCodigo(3);
               return loginResponse;
           }
       }else{
           loginResponse.setCodigo(2);
           loginResponse.setMensaje("Usuario no existe");
           return loginResponse;
       }

    }





}
