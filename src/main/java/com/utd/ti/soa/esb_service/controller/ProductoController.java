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

import com.utd.ti.soa.esb_service.model.Client;
import com.utd.ti.soa.esb_service.model.Productos;
import com.utd.ti.soa.esb_service.utils.Auth;

@RestController
@RequestMapping("/esb")
public class ProductoController {
    private final WebClient webClient = WebClient.create();
    private final Auth auth = new Auth();

    // Crear producto
    @PostMapping("/producto")
    public ResponseEntity createClient(@RequestBody Productos producto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Request Body: " + producto);
        System.out.println("Token recibido: " + token);

        // Validar token
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición al servicio de clientes
        String response = webClient.post()
            .uri("http://localhost:5003/app/products/create")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue(producto))
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    }

    // Obtener todos los productos
    @GetMapping("/producto")
    public ResponseEntity getClients(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);
        
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición al servicio de clientes
        String response = webClient.get()
            .uri("http://localhost:5003/app/products/")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    }

    // Obtener producto por id
    @GetMapping("/producto/{id}")
    public ResponseEntity getClientById(@PathVariable String id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);
        
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición al servicio de clientes
        String response = webClient.get()
            .uri("http://localhost:5003/app/products/" + id)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    }

    // Actualizar producto
    @PatchMapping("/producto/update/{id}")
    public ResponseEntity updateClient(@PathVariable String id,
            @RequestBody Productos producto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Request Body: " + producto);
        System.out.println("Token recibido: " + token);
        System.out.println("ID: " + id);

        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        // Enviar petición al servicio de clientes
        String response = webClient.patch()
            .uri("http://localhost:5003/app/products/update/" + id)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue(producto))
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    }

    // Eliminar producto
    @PatchMapping("/producto/delete/{id}")
    public ResponseEntity<?> patchProductStatus(@PathVariable String id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);
        
        // Validar token
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición PATCH al servicio de productos para actualizar el estatus
        String response = webClient.patch()
            .uri("http://localhost:5003/app/products/delete/" + id)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
}

    //Actualizar stock de producto
    @PatchMapping("/producto/stock/{id}/{stock}")
    public ResponseEntity updateStock(@PathVariable String id,
            @PathVariable String stock,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);
        
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición al servicio de clientes
        String response = webClient.patch()
            .uri("http://localhost:5003/app/products/" + id + "/" + stock)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    }
    
}
