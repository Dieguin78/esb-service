package com.utd.ti.soa.esb_service.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.utd.ti.soa.esb_service.model.User;
import com.utd.ti.soa.esb_service.utils.Auth;

@RestController
@RequestMapping("/esb")
public class ESBController {
    
    private final WebClient webClient;
    private final Auth auth;

    public ESBController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://users-production-2a02.up.railway.app/app").build();
        this.auth = new Auth();
    }

    // Crear usuario
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        System.out.println("🔹 Creando usuario: " + user);
        System.out.println("🔐 Token recibido: " + token);

        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401).body("❌ Token inválido o expirado");
        }

        try {
            String response = webClient.post()
                .uri("/users/create")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(user))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error en la petición: " + e.getMessage());
        }
    }

    // Obtener todos los usuarios
    @GetMapping("/user")
    public ResponseEntity<String> getUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("📥 Solicitando lista de usuarios...");
        System.out.println("🔐 Token recibido: " + token);

        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401).body("❌ Token inválido o expirado");
        }

        try {
            String response = webClient.get()
                .uri("/users/all")
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error en la petición: " + e.getMessage());
        }
    }

    // Actualizar usuario
    @PatchMapping("/user/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id,
            @RequestBody User user,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        System.out.println("🛠️ Actualizando usuario ID: " + id);
        System.out.println("🔐 Token recibido: " + token);

        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401).body("❌ Token inválido o expirado");
        }

        try {
            String response = webClient.patch()
                .uri("/users/update/" + id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(user))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error en la petición: " + e.getMessage());
        }
    }

    // Eliminar usuario
    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        System.out.println("🗑️ Eliminando usuario ID: " + id);
        System.out.println("🔐 Token recibido: " + token);

        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401).body("❌ Token inválido o expirado");
        }

        try {
            String response = webClient.delete()
                .uri("/users/delete/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error en la petición: " + e.getMessage());
        }
    }
}
