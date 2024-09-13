package com.puente.web.controller;

import com.puente.service.dto.LoginDto;
import com.puente.web.config.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticación", description = "Este controlador permite poder autenticarse y devolver un JWT (JSON Web Token).")
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * @description Método que se utiliza para poder autenticarse y obtener el JWT (JSON Web Token)
     * @param loginDto Recibe un objeto de tipo LoginDto, el cual contiene el usuario y contraseña que
     *                 se utiliza para poder autenticarse.
     * @return Void No retorna ningún.
     * */
    @Operation(summary = "Autenticación", description = "Método que se usa para poder autenticarse y así consumir" +
            " los endpoints.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se ha autenticado con éxito, el JWT se puede obtener desde el HEADERS."),
            @ApiResponse(responseCode = "400", description = "Las credenciales dentro de la petición son incorrectas.")
    })
    @Parameters({
            @Parameter(name = "username", description = "Nombre del usuario", example = "miUsuario"),
            @Parameter(name = "password", description = "Contraseña del usuario", example = "miContraseña")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);

        if (authentication.isAuthenticated()){
            String jwt = this.jwtUtil.create(loginDto.getUsername());
            return ResponseEntity.ok(jwt);
        }else{
            return ResponseEntity.status(400).body("Error en credenciales.");
        }

        //return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }
}
