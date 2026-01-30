package org.vaargcapital.evolve.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.vaargcapital.evolve.api.EvolveApi;
import org.vaargcapital.evolve.service.EvolveService;
import reactor.core.publisher.Mono;

@RestController
@Validated
public class EvolveController implements EvolveApi {

    private final EvolveService evolveService;

    public EvolveController(EvolveService evolveService) {
        this.evolveService = evolveService;
    }

    @Override
    public Mono<ResponseEntity<Void>> access(String grantor, String grantee, Integer level, String email, ServerWebExchange exchange) {
        return evolveService.handleAccess(grantor, grantee, email, level)
                .thenReturn(ResponseEntity.ok().build());
    }

    @Override
    public Mono<ResponseEntity<Void>> disCode(String nickname, String message, String ip, ServerWebExchange exchange) {
        return evolveService.handleDisCode(nickname, message, ip)
                .thenReturn(ResponseEntity.ok().build());
    }

    @Override
    public Mono<ResponseEntity<Void>> login(String ip, String nickname, ServerWebExchange exchange) {
        return evolveService.handleLogin(ip, nickname)
                .thenReturn(ResponseEntity.ok().build());
    }

    @Override
    public Mono<ResponseEntity<Void>> punishment(String submittedBy, String violator, Integer ptype, String violation, ServerWebExchange exchange) {
        return evolveService.handlePunishment(submittedBy, violator, ptype, violation)
                .thenReturn(ResponseEntity.ok().build());
    }

}
