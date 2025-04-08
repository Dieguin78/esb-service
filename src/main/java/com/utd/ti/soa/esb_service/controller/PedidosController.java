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

import com.utd.ti.soa.esb_service.model.Pedido;
import com.utd.ti.soa.esb_service.utils.Auth;

@RestController
@RequestMapping("/esb")
public class PedidosController {
    private final WebClient webClient = WebClient.create();
    private final Auth auth = new Auth();
    
    // Crear pedido
    @PostMapping("/pedidos")
    public ResponseEntity createPedido(@RequestBody Pedido pedido,
    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Request Body: " + pedido);
        System.out.println("Token recibido: " + token);

        // Validar token
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }

        // Enviar petición al servicio de pedidos
        String response = webClient.post()
            .uri("http://localhost:5002/app/pedidos/crear")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(BodyInserters.fromValue(pedido))
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    };

    // Obtener todos los pedidos
    @GetMapping("/pedidos")
    public ResponseEntity getPedidos(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);
        
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición al servicio de pedidos
        String response = webClient.get()
            .uri("http://localhost:5002/app/pedidos/")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    };

    // Obtener pedido por ID
    @GetMapping("/pedidos/{id}")
    public ResponseEntity getPedidoById(@PathVariable String id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);
        
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición al servicio de pedidos
        String response = webClient.get()
            .uri("http://localhost:5002/app/pedidos/" + id)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    };

    //Actualizar estado pedido
    @PatchMapping("/pedidos/{id}/{estado}")
    public ResponseEntity updatePedido(@PathVariable String id,
            @PathVariable String estado,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);
        
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición al servicio de pedidos
        String response = webClient.patch()
            .uri("http://localhost:5002/app/pedidos/" + id + "/" + estado)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    };

    //cancelar pedido
    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity deletePedido(@PathVariable String id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Token recibido: " + token);
        
        if (!auth.validateToken(token)) {
            return ResponseEntity.status(401)
                .body("Token inválido o expirado");
        }
        
        // Enviar petición al servicio de pedidos
        String response = webClient.delete()
            .uri("http://localhost:5002/app/pedidos/" + id)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> System.out.println("Error: " + error.getMessage()))
            .block();
        
        return ResponseEntity.ok(response);
    };
}
