package com.utd.ti.soa.esb_service.controller;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.utd.ti.soa.esb_service.model.categorias;
import com.utd.ti.soa.esb_service.utils.Auth;

@RestController
@RequestMapping("/esb")
public class categoriaController {
    private final WebClient webClient = WebClient.create();
    private final Auth auth = new Auth();

    // Crear categoria
    @PostMapping("/categoria")
    public ResponseEntity createCategoria(@RequestBody categorias categoria,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Request Body: " + categoria);
        System.out.println("Token recibido: " + token);

        // Validar token
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición al servicio de categorias
        String response = webClient.post()
            .uri("http://localhost:5002/app/categories/create")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue(categoria))
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    }

    // Elimar categoria por id
    @DeleteMapping("/categoria/{id}")
    public ResponseEntity deleteCategoria(@PathVariable String id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("ID recibido: " + id);
        System.out.println("Token recibido: " + token);

        // Validar token
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }

        // Enviar petición al servicio de categorias
        String response = webClient.delete()
            .uri("http://localhost:5002/app/categories/" + id)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    }

    //Obtener categorias
    @GetMapping("/categoria")
    public ResponseEntity getCategorias(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);

        // Validar token
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }

        // Enviar petición al servicio de categorias
        String response = webClient.get()
            .uri("http://localhost:5002/app/categories")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    }
}
